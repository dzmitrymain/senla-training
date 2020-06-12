package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Comparator;
import java.util.Date;

public interface BookService {

    void replenishBook(Book book);

    void writeOffBook(Book book);

    Book[] findAllBooks(Comparator<Book> bookComparator);

    Book[] findSoldBooksBetweenDates(Date startDate, Date endDate);

    Book[] findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    Book[] findStaleBooks();

    BookDescription showBookDescription(Book book);
}
