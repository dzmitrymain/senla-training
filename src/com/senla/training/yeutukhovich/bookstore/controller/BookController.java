package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
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

    private BookController() {

    }

    public String replenishBook(Long id) {
        if (bookService.replenishBook(id)) {
            return MessageConstant.BOOK_HAS_BEEN_REPLENISHED.getMessage();
        }
        return MessageConstant.BOOK_HAS_NOT_BEEN_REPLENISHED.getMessage();
    }

    public String writeOffBook(Long id) {
        if (bookService.writeOffBook(id)) {
            return MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF.getMessage();
        }
        return MessageConstant.BOOK_HAS_NOT_BEEN_WRITTEN_OFF.getMessage();
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

    public BookDescription showBookDescription(Long id) {
        return bookService.showBookDescription(id);
    }

    public int importBooks(String fileName) {
        return bookService.importBooks(fileName);
    }

    public int exportAllBooks(String fileName) {
        return bookService.exportAllBooks(fileName);
    }

    public boolean exportBook(Long bookId, String fileName) {
        return bookService.exportBook(bookId, fileName);
    }
}
