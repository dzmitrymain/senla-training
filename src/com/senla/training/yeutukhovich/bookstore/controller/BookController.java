package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class BookController {

    private static final String BOOKS_DELIMITER = "\n";

    @Autowired
    private BookService bookService;

    private BookController() {

    }

    public String replenishBook(Long id) {
        try {
            bookService.replenishBook(id);
            return MessageConstant.BOOK_HAS_BEEN_REPLENISHED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        }
    }

    public String writeOffBook(Long id) {
        try {
            bookService.writeOffBook(id);
            return MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        }
    }

    public String findSortedAllBooksByAvailability() {
        return bookService.findSortedAllBooksByAvailability().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findSortedAllBooksByEditionDate() {
        return bookService.findSortedAllBooksByEditionDate().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findSortedAllBooksByPrice() {
        return bookService.findSortedBooksByPrice().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findSortedAllBooksByReplenishmentDate() {
        return bookService.findSortedAllBooksByReplenishmentDate().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findSortedAllBooksByTitle() {
        return bookService.findSortedAllBooksByTitle().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findSoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findSoldBooksBetweenDates(startDate, endDate).stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String findStaleBooks() {
        return bookService.findStaleBooks().stream()
                .map(Book::toString)
                .collect(Collectors.joining(BOOKS_DELIMITER));
    }

    public String showBookDescription(Long id) {
        Optional<BookDescription> bookDescriptionOptional = bookService.showBookDescription(id);
        if (bookDescriptionOptional.isEmpty()) {
            return MessageConstant.BOOK_DESCRIPTION_WAS_NOT_FOUND.getMessage();
        }
        return bookDescriptionOptional.get().toString();
    }

    public String importBooks(String fileName) {
        try {
            return MessageConstant.IMPORTED_ENTITIES.getMessage() + bookService.importBooks(fileName);
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllBooks(String fileName) {
        try {
            return MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + bookService.exportAllBooks(fileName);
        } catch (InternalException e) {
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
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
