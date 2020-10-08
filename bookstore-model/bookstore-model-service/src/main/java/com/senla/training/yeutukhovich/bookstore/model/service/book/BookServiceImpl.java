package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCsvConverter;
import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.DtoMapper;
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
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCsvConverter entityCsvConverter;

    @Value("${BookServiceImpl.requestAutoCloseEnabled:true}")
    private boolean requestAutoCloseEnabled;
    @Value("${BookServiceImpl.staleMonthNumber:6}")
    private byte staleMonthNumber;
    private String csvDirectoryPath = ApplicationConstant.CSV_DIRECTORY_PATH;

    @Override
    @Transactional
    public BookDto replenishBook(Long id) {
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
        return DtoMapper.mapBook(book);
    }

    @Override
    @Transactional
    public BookDto writeOffBook(Long id) {
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
        return DtoMapper.mapBook(book);
    }

    @Override
    @Transactional
    public List<BookDto> findSortedAllBooksByAvailability() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_AVAILABILITY.getMessage());
        return bookDao.findSortedAllBooksByAvailability().stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findSortedAllBooksByEditionYear() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_EDITION_YEAR.getMessage());
        return bookDao.findSortedAllBooksByEditionYear().stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findSortedAllBooksByPrice() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_PRICE.getMessage());
        return bookDao.findSortedAllBooksByPrice().stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findSortedAllBooksByReplenishmentDate() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_REPLENISHMENT_DATE.getMessage());
        return bookDao.findSortedAllBooksByReplenishmentDate().stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findSortedAllBooksByTitle() {
        LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_TITLE.getMessage());
        return bookDao.findSortedAllBooksByTitle().stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.FIND_SOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return bookDao.findSoldBooksBetweenDates(startDate, endDate).stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.FIND_UNSOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return bookDao.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookDto> findStaleBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
        Date staleDate = new Date(calendar.getTimeInMillis());
        LOGGER.info(LoggerConstant.FIND_STALE_BOOKS.getMessage());
        return bookDao.findStaleBooksBetweenDates(staleDate, new Date()).stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDescriptionDto showBookDescription(Long id) {
        Book book = bookDao.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.SHOW_BOOK_DESCRIPTION_FAIL.getMessage(), id,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
                });
        BookDescriptionDto bookDescriptionDto = new BookDescriptionDto();
        bookDescriptionDto.setTitle(book.getTitle());
        bookDescriptionDto.setEditionYear(book.getEditionYear());
        bookDescriptionDto.setReplenishmentDate(book.getReplenishmentDate());
        LOGGER.info(LoggerConstant.SHOW_BOOK_DESCRIPTION_SUCCESS.getMessage(), id);
        return bookDescriptionDto;
    }

    @Override
    @Transactional
    public List<BookDto> exportAllBooks(String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        List<Book> books = bookDao.findAll();
        List<String> bookStrings = entityCsvConverter.convertBooks(books);
        FileDataWriter.writeData(path, bookStrings);
        LOGGER.info(LoggerConstant.EXPORT_ALL_BOOKS.getMessage(), fileName);
        return books.stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BookDto exportBook(Long bookId, String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        Book book = bookDao.findById(bookId).orElseThrow(() -> {
            LOGGER.warn(LoggerConstant.EXPORT_BOOK_FAIL.getMessage(), bookId, MessageConstant.BOOK_NOT_EXIST.getMessage());
            return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        });
        List<String> bookStrings = entityCsvConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
        LOGGER.info(LoggerConstant.EXPORT_BOOK_SUCCESS.getMessage(), bookId, fileName);
        return DtoMapper.mapBook(book);
    }

    @Override
    @Transactional
    public List<BookDto> importBooks(String fileName) {
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> importedBooks = entityCsvConverter.parseBooks(dataStrings);
        for (Book importedBook : importedBooks) {
            bookDao.update(importedBook);
            if (importedBook.isAvailable()) {
                requestDao.closeRequestsByBookId(importedBook.getId());
            }
        }
        LOGGER.info(LoggerConstant.IMPORT_BOOKS_SUCCESS.getMessage(), fileName);
        return importedBooks.stream()
                .map(DtoMapper::mapBook)
                .collect(Collectors.toList());
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
