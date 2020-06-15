package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

public interface BookService {

    boolean replenishBook(Long id);

    boolean writeOffBook(Long id);

    List<Book> findAllBooks(Comparator<Book> bookComparator);

    List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findStaleBooks();

    BookDescription showBookDescription(Long id);
}
