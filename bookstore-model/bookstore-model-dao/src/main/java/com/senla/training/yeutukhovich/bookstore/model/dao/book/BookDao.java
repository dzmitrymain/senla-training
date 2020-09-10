package com.senla.training.yeutukhovich.bookstore.model.dao.book;

import com.senla.training.yeutukhovich.bookstore.model.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;

import java.util.Date;
import java.util.List;

public interface BookDao extends GenericDao<Book, Long> {

    List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findStaleBooksBetweenDates(Date startDate, Date endDate);

    List<Book> findSortedAllBooksByAvailability();

    List<Book> findSortedAllBooksByEditionYear();

    List<Book> findSortedAllBooksByPrice();

    List<Book> findSortedAllBooksByReplenishmentDate();

    List<Book> findSortedAllBooksByTitle();
}
