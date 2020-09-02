package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.dao.connector.DbConnector;
import com.senla.training.yeutukhovich.bookstore.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookServiceImpl implements BookService {

    @Autowired
    private DbConnector connector;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;
    @ConfigProperty
    private boolean requestAutoCloseEnabled;
    @ConfigProperty
    private byte staleMonthNumber;

    @Override
    public void replenishBook(Long id) {
        Connection connection = connector.getConnection();
        Book book = bookDao.findById(connection, id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));

        if (book.isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        book.setAvailable(true);
        book.setReplenishmentDate(new Date());
        try {
            connection.setAutoCommit(false);
            try {
                bookDao.update(connection, book);
                if (requestAutoCloseEnabled) {
                    requestDao.closeRequestsByBookId(connection, book.getId());
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public void writeOffBook(Long id) {
        Connection connection = connector.getConnection();
        Book book = bookDao.findById(connection, id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        if (!book.isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
        }
        book.setAvailable(false);
        bookDao.update(connection, book);
    }


    @Override
    public List<Book> findSortedAllBooksByAvailability() {
        return bookDao.findSortedAllBooksByAvailability(connector.getConnection());
    }

    @Override
    public List<Book> findSortedAllBooksByEditionYear() {
        return bookDao.findSortedAllBooksByEditionYear(connector.getConnection());
    }

    @Override
    public List<Book> findSortedAllBooksByPrice() {
        return bookDao.findSortedAllBooksByPrice(connector.getConnection());
    }

    @Override
    public List<Book> findSortedAllBooksByReplenishmentDate() {
        return bookDao.findSortedAllBooksByReplenishmentDate(connector.getConnection());
    }

    @Override
    public List<Book> findSortedAllBooksByTitle() {
        return bookDao.findSortedAllBooksByTitle(connector.getConnection());
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookDao.findSoldBooksBetweenDates(connector.getConnection(), startDate, endDate);
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookDao.findUnsoldBooksBetweenDates(connector.getConnection(), startDate, endDate);
    }

    @Override
    public List<Book> findStaleBooks() {
        Connection connection = connector.getConnection();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
        Date staleDate = new Date(calendar.getTimeInMillis());
        return bookDao.findStaleBooksBetweenDates(connection, staleDate, new Date());
    }

    @Override
    public BookDescription showBookDescription(Long id) {
        Connection connection = connector.getConnection();
        Book book = bookDao.findById(connection, id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        BookDescription bookDescription = new BookDescription();
        bookDescription.setTitle(book.getTitle());
        bookDescription.setEditionYear(book.getEditionYear());
        bookDescription.setReplenishmentDate(book.getReplenishmentDate());
        return bookDescription;
    }

    @Override
    public int exportAllBooks(String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> bookStrings = entityCvsConverter.convertBooks(bookDao.findAll(connection));
        return FileDataWriter.writeData(path, bookStrings);
    }

    @Override
    public void exportBook(Long bookId, String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Book> bookOptional = bookDao.findById(connection, bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
    }

    @Override
    public int importBooks(String fileName) {
        Connection connection = connector.getConnection();
        if (fileName == null) {
            return 0;
        }
        int importedBooksNumber = 0;
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> repoBooks = bookDao.findAll(connection);
        List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);
        try {
            connection.setAutoCommit(false);
            try {
                for (Book importedBook : importedBooks) {
                    if (repoBooks.contains(importedBook)) {
                        bookDao.update(connection, importedBook);
                        updateRequestsAfterImport(connection, importedBook);
                    } else {
                        bookDao.add(connection, importedBook);
                    }
                    importedBooksNumber++;
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
        return importedBooksNumber;
    }

    private List<Book> findAllBooks() {
        Connection connection = connector.getConnection();
        return bookDao.findAll(connection);
    }

    private void updateRequestsAfterImport(Connection connection, Book book) {
        if (book.isAvailable()) {
            requestDao.closeRequestsByBookId(connection, book.getId());
        }
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        return FileDataReader.readData(path);
    }
}
