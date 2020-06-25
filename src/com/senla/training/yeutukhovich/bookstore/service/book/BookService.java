package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Date;
import java.util.List;

public interface BookService {

    boolean replenishBook(Long id);

    boolean writeOffBook(Long id);

    List<String> findSortedAllBooksByAvailability();

    List<String> findSortedAllBooksByEditionDate();

    List<String> findSortedBooksByPrice();

    List<String> findSortedAllBooksByReplenishmentDate();

    List<String> findSortedAllBooksByTitle();

    List<String> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<String> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<String> findStaleBooks();

    BookDescription showBookDescription(Long id);

    int exportAllBooks(String fileName);

    boolean exportBook(Long bookId, String fileName);

    int importBooks(String fileName);
}
