package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.util.HibernateUtil;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private HibernateUtil hibernateUtil;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @Value("${csv.directoryPath:resources/cvs/}")
    private String cvsDirectoryPath;
    @Value("${BookServiceImpl.requestAutoCloseEnabled:true}")
    private boolean requestAutoCloseEnabled;
    @Value("${BookServiceImpl.staleMonthNumber:6}")
    private byte staleMonthNumber;

    @Transactional
    @Override
    public void replenishBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        if (book.isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        book.setAvailable(true);
        book.setReplenishmentDate(new Date());
        bookDao.update(book);
        if (requestAutoCloseEnabled) {
            requestDao.closeRequestsByBookId(book.getId());
        }
    }

    @Transactional
    @Override
    public void writeOffBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        if (!book.isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
        }
        book.setAvailable(false);
        bookDao.update(book);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSortedAllBooksByAvailability() {
        return bookDao.findSortedAllBooksByAvailability();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSortedAllBooksByEditionYear() {
        return bookDao.findSortedAllBooksByEditionYear();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSortedAllBooksByPrice() {
        return bookDao.findSortedAllBooksByPrice();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSortedAllBooksByReplenishmentDate() {
        return bookDao.findSortedAllBooksByReplenishmentDate();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSortedAllBooksByTitle() {
        return bookDao.findSortedAllBooksByTitle();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookDao.findSoldBooksBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookDao.findUnsoldBooksBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> findStaleBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
        Date staleDate = new Date(calendar.getTimeInMillis());
        return bookDao.findStaleBooksBetweenDates(staleDate, new Date());
    }

    @Transactional(readOnly = true)
    @Override
    public BookDescription showBookDescription(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        BookDescription bookDescription = new BookDescription();
        bookDescription.setTitle(book.getTitle());
        bookDescription.setEditionYear(book.getEditionYear());
        bookDescription.setReplenishmentDate(book.getReplenishmentDate());
        return bookDescription;
    }

    @Transactional(readOnly = true)
    @Override
    public int exportAllBooks(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        List<String> bookStrings = entityCvsConverter.convertBooks(bookDao.findAll());
        return FileDataWriter.writeData(path, bookStrings);
    }

    @Transactional(readOnly = true)
    @Override
    public void exportBook(Long bookId, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        Optional<Book> bookOptional = bookDao.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
    }

    @Transactional
    @Override
    public int importBooks(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedBooksNumber = 0;
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);
        List<Book> repoBooks = bookDao.findAll();
//                session.clear();
        for (Book importedBook : importedBooks) {
            if (repoBooks.contains(importedBook)) {
                bookDao.update(importedBook);
                if (importedBook.isAvailable()) {
                    requestDao.closeRequestsByBookId(importedBook.getId());
                }
            } else {
                bookDao.add(importedBook);
            }
            importedBooksNumber++;
        }
        return importedBooksNumber;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
