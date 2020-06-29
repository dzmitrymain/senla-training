package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.service.book.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookController {

    private static BookController instance;

    private BookService bookService;

    private BookController(BookService bookService) {
        this.bookService = bookService;
    }

    public static BookController getInstance() {
        if (instance == null) {
            instance = new BookController(BookServiceImpl.getInstance());
        }
        return instance;
    }

    public boolean replenishBook(Long id) {
        return bookService.replenishBook(id);
    }

    public boolean writeOffBook(Long id) {
        return bookService.writeOffBook(id);
    }

    public List<String> findSortedAllBooksByAvailability() {
        return bookService.findSortedAllBooksByAvailability().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByEditionDate() {
        return bookService.findSortedAllBooksByEditionDate().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByPrice() {
        return bookService.findSortedBooksByPrice().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByReplenishmentDate() {
        return bookService.findSortedAllBooksByReplenishmentDate().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByTitle() {
        return bookService.findSortedAllBooksByTitle().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }


    public List<String> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findSoldBooksBetweenDates(startDate, endDate).stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }


    public List<String> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findStaleBooks() {
        return bookService.findStaleBooks().stream()
                .map(Book::toString)
                .collect(Collectors.toList());
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
