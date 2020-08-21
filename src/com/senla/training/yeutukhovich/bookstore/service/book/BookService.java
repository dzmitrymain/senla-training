package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookService {

    void replenishBook(Long id);

    void writeOffBook(Long id);

    List<Book> findSortedAllBooksByAvailability();

    List<Book> findSortedAllBooksByEditionDate();

    List<Book> findSortedAllBooksByPrice();

    List<Book> findSortedAllBooksByReplenishmentDate();

    List<Book> findSortedAllBooksByTitle();

    List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findStaleBooks();

    Optional<BookDescription> showBookDescription(Long id);

    int exportAllBooks(String fileName);

    void exportBook(Long bookId, String fileName);

    int importBooks(String fileName);
}
