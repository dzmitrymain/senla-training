package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @Value("${BookServiceImpl.requestAutoCloseEnabled:true}")
    private boolean requestAutoCloseEnabled;
    @Value("${BookServiceImpl.staleMonthNumber:6}")
    private byte staleMonthNumber;
    private String cvsDirectoryPath = ApplicationConstant.CVS_DIRECTORY_PATH;

    @Override
    @Transactional
    public Book replenishBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.REPLENISH_BOOK_FAIL.getMessage(), id,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
                });
        if (book.isAvailable()) {
            LOGGER.warn(LoggerConstant.REPLENISH_BOOK_FAIL.getMessage(), id,
                    MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        book.setAvailable(true);
        book.setReplenishmentDate(new Date());
        bookDao.update(book);
        LOGGER.info(LoggerConstant.REPLENISH_BOOK_SUCCESS.getMessage(), id);
        if (requestAutoCloseEnabled) {
            requestDao.closeRequestsByBookId(book.getId());
            LOGGER.info(LoggerConstant.REQUESTS_CLOSED.getMessage(), id);
        }
        return book;
    }

    @Override
    @Transactional
    public Book writeOffBook(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.WRITE_OFF_BOOK_FAIL.getMessage(), id,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
                });
        if (!book.isAvailable()) {
            LOGGER.warn(LoggerConstant.WRITE_OFF_BOOK_FAIL.getMessage(), id,
                    MessageConstant.BOOK_NOT_AVAILABLE.getMessage());
            throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
        }
        book.setAvailable(false);
        bookDao.update(book);
        LOGGER.info(LoggerConstant.WRITE_OFF_BOOK_SUCCESS.getMessage(), id);
        return book;
    }

    @Override
    @Transactional
    public List<Book> findSortedAllBooksByAvailability() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_AVAILABILITY.getMessage());
        return bookDao.findSortedAllBooksByAvailability();
    }

    @Override
    @Transactional
    public List<Book> findSortedAllBooksByEditionYear() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_EDITION_YEAR.getMessage());
        return bookDao.findSortedAllBooksByEditionYear();
    }

    @Override
    @Transactional
    public List<Book> findSortedAllBooksByPrice() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_PRICE.getMessage());
        return bookDao.findSortedAllBooksByPrice();
    }

    @Override
    @Transactional
    public List<Book> findSortedAllBooksByReplenishmentDate() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_REPLENISHMENT_DATE.getMessage());
        return bookDao.findSortedAllBooksByReplenishmentDate();
    }

    @Override
    @Transactional
    public List<Book> findSortedAllBooksByTitle() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_TITLE.getMessage());
        return bookDao.findSortedAllBooksByTitle();
    }

    @Override
    @Transactional
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.FIND_SOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return bookDao.findSoldBooksBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.FIND_UNSOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return bookDao.findUnsoldBooksBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional
    public List<Book> findStaleBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
        Date staleDate = new Date(calendar.getTimeInMillis());
        LOGGER.info(LoggerConstant.FIND_STALE_BOOKS.getMessage());
        return bookDao.findStaleBooksBetweenDates(staleDate, new Date());
    }

    @Override
    @Transactional
    public BookDescription showBookDescription(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.SHOW_BOOK_DESCRIPTION_FAIL.getMessage(), id,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
                });
        BookDescription bookDescription = new BookDescription();
        bookDescription.setTitle(book.getTitle());
        bookDescription.setEditionYear(book.getEditionYear());
        bookDescription.setReplenishmentDate(book.getReplenishmentDate());
        LOGGER.info(LoggerConstant.SHOW_BOOK_DESCRIPTION_SUCCESS.getMessage(), id);
        return bookDescription;
    }

    @Override
    @Transactional
    public List<Book> exportAllBooks(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        List<Book> books = bookDao.findAll();
        List<String> bookStrings = entityCvsConverter.convertBooks(books);
        FileDataWriter.writeData(path, bookStrings);
        LOGGER.info(LoggerConstant.EXPORT_ALL_BOOKS.getMessage(), fileName);
        return books;
    }

    @Override
    @Transactional
    public Book exportBook(Long bookId, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        Book book = bookDao.findById(bookId).orElseThrow(() -> {
            LOGGER.warn(LoggerConstant.EXPORT_BOOK_FAIL.getMessage(), bookId, MessageConstant.BOOK_NOT_EXIST.getMessage());
            return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        });
        List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
        LOGGER.info(LoggerConstant.EXPORT_BOOK_SUCCESS.getMessage(), bookId, fileName);
        return book;
    }

    @Override
    @Transactional
    public List<Book> importBooks(String fileName) {
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);
        for (Book importedBook : importedBooks) {
            bookDao.update(importedBook);
            if (importedBook.isAvailable()) {
                requestDao.closeRequestsByBookId(importedBook.getId());
            }
        }
        LOGGER.info(LoggerConstant.IMPORT_BOOKS_SUCCESS.getMessage(), fileName);
        return importedBooks;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
