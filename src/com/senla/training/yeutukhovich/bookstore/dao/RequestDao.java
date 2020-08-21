package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.sql.Connection;

public interface RequestDao extends GenericDao<Request> {

    Long closeRequestsByBookId(Connection connection, Long bookId);
}
