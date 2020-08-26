package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.Date;
import java.util.stream.Collectors;

@Singleton
public class BookController {

    private static final String BOOKS_DELIMITER = "\n";

    @Autowired
    private BookService bookService;

    public String replenishBook(Long id) {
        try {
            bookService.replenishBook(id);
            return MessageConstant.BOOK_HAS_BEEN_REPLENISHED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String writeOffBook(Long id) {
        try {
            bookService.writeOffBook(id);
            return MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByAvailability() {
        try {
            return bookService.findSortedAllBooksByAvailability().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByEditionYear() {
        try {
            return bookService.findSortedAllBooksByEditionYear().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByPrice() {
        try {
            return bookService.findSortedAllBooksByPrice().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByReplenishmentDate() {
        try {
            return bookService.findSortedAllBooksByReplenishmentDate().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllBooksByTitle() {
        try {
            return bookService.findSortedAllBooksByTitle().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSoldBooksBetweenDates(Date startDate, Date endDate) {
        try {
            return bookService.findSoldBooksBetweenDates(startDate, endDate).stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        try {
            return bookService.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findStaleBooks() {
        try {
            return bookService.findStaleBooks().stream()
                    .map(Book::toString)
                    .collect(Collectors.joining(BOOKS_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String showBookDescription(Long id) {
        try {
            return bookService.showBookDescription(id).toString();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importBooks(String fileName) {
        try {
            return MessageConstant.IMPORTED_ENTITIES.getMessage() + bookService.importBooks(fileName);
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllBooks(String fileName) {
        try {
            return MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + bookService.exportAllBooks(fileName);
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportBook(Long bookId, String fileName) {
        try {
            bookService.exportBook(bookId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
