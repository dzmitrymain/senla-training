package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.stream.Collectors;

@Controller
public class BookController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private static final String BOOKS_DELIMITER = "\n";

    @Autowired
    private BookService bookService;

    public String replenishBook(Long id) {
        try {
            bookService.replenishBook(id);
            LOGGER.info(LoggerConstant.REPLENISH_BOOK_SUCCESS.getMessage(), id);
            return MessageConstant.BOOK_HAS_BEEN_REPLENISHED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.REPLENISH_BOOK_FAIL.getMessage(), id, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String writeOffBook(Long id) {
        try {
            bookService.writeOffBook(id);
            LOGGER.info(LoggerConstant.WRITE_OFF_BOOK_SUCCESS.getMessage(), id);
            return MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.WRITE_OFF_BOOK_FAIL.getMessage(), id, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByAvailability() {
        try {
            String result = bookService.findSortedAllBooksByAvailability().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_AVAILABILITY.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByEditionYear() {
        try {
            String result = bookService.findSortedAllBooksByEditionYear().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_EDITION_YEAR.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByPrice() {
        try {
            String result = bookService.findSortedAllBooksByPrice().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_PRICE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByReplenishmentDate() {
        try {
            String result = bookService.findSortedAllBooksByReplenishmentDate().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_REPLENISHMENT_DATE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByTitle() {
        try {
            String result = bookService.findSortedAllBooksByTitle().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_BOOKS_SORTED_BY_TITLE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSoldBooksBetweenDates(Date startDate, Date endDate) {
        try {
            String result = bookService.findSoldBooksBetweenDates(startDate, endDate).stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_SOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                    DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        try {
            String result = bookService.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_UNSOLD_BOOKS.getMessage(), DateConverter.formatDate(startDate,
                    DateConverter.DAY_DATE_FORMAT), DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findStaleBooks() {
        try {
            String result = bookService.findStaleBooks().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_STALE_BOOKS.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String showBookDescription(Long id) {
        try {
            String result = bookService.showBookDescription(id).toString();
            LOGGER.info(LoggerConstant.SHOW_BOOK_DESCRIPTION_SUCCESS.getMessage(), id);
            return result;
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.SHOW_BOOK_DESCRIPTION_FAIL.getMessage(), id, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importBooks(String fileName) {
        try {
            String result = MessageConstant.IMPORTED_ENTITIES.getMessage() + bookService.importBooks(fileName);
            LOGGER.info(LoggerConstant.IMPORT_BOOKS_SUCCESS.getMessage(), fileName);
            return result;
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.IMPORT_BOOKS_FAIL.getMessage(), e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllBooks(String fileName) {
        try {
            String result = MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + bookService.exportAllBooks(fileName);
            LOGGER.info(LoggerConstant.EXPORT_ALL_BOOKS.getMessage(), fileName);
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportBook(Long bookId, String fileName) {
        try {
            bookService.exportBook(bookId, fileName);
            LOGGER.info(LoggerConstant.EXPORT_BOOK_SUCCESS.getMessage(), bookId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.EXPORT_BOOK_FAIL.getMessage(), bookId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
