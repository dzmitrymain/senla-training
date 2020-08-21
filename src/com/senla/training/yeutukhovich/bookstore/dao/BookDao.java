package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public interface BookDao extends GenericDao<Book> {

    List<Book> findSoldBooksBetweenDates(Connection connection, Date startDate, Date endDate);
    List<Book> findUnsoldBooksBetweenDates(Connection connection, Date startDate, Date endDate);
    List<Book> findStaleBooksBetweenDates(Connection connection, Date startDate, Date endDate);
}
