package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.bookservice.BookService;
import com.senla.training.yeutukhovich.bookstore.service.bookservice.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

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


    public List<Book> findAllBooks(Comparator<Book> bookComparator) {
        return bookService.findAllBooks(bookComparator);
    }


    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findSoldBooksBetweenDates(startDate, endDate);
    }


    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        return bookService.findUnsoldBooksBetweenDates(startDate, endDate);
    }


    public List<Book> findStaleBooks() {
        return bookService.findStaleBooks();
    }


    public BookDescription showBookDescription(Long id) {
        return bookService.showBookDescription(id);
    }
}
