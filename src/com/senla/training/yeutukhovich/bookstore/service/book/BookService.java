package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Date;
import java.util.List;

public interface BookService {

    boolean replenishBook(Long id);

    boolean writeOffBook(Long id);

    List<Book> findSortedAllBooksByAvailability();

    List<Book> findSortedAllBooksByEditionDate();

    List<Book> findSortedBooksByPrice();

    List<Book> findSortedAllBooksByReplenishmentDate();

    List<Book> findSortedAllBooksByTitle();

    List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findStaleBooks();

    BookDescription showBookDescription(Long id);

    int exportAllBooks(String fileName);

    boolean exportBook(Long bookId, String fileName);

    int importBooks(String fileName);
}
