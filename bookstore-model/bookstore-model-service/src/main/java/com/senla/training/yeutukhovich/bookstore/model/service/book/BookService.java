package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;


import java.util.Date;
import java.util.List;

public interface BookService {

    Book replenishBook(Long id);

    Book writeOffBook(Long id);

    List<Book> findSortedAllBooksByAvailability();

    List<Book> findSortedAllBooksByEditionYear();

    List<Book> findSortedAllBooksByPrice();

    List<Book> findSortedAllBooksByReplenishmentDate();

    List<Book> findSortedAllBooksByTitle();

    List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findStaleBooks();

    BookDescriptionDto showBookDescription(Long id);

    List<Book> exportAllBooks(String fileName);

    Book exportBook(Long bookId, String fileName);

    List<Book> importBooks(String fileName);
}
