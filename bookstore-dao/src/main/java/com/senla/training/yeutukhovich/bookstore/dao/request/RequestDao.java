package com.senla.training.yeutukhovich.bookstore.dao.request;

import com.senla.training.yeutukhovich.bookstore.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.sql.Connection;
import java.util.List;

public interface RequestDao extends GenericDao<Request> {

    Long closeRequestsByBookId(Connection connection, Long bookId);

    List<Request> findSortedAllRequestsByBookTitle(Connection connection);

    List<Request> findSortedAllRequestsByIsActive(Connection connection);

    List<Request> findSortedAllRequestsByRequesterData(Connection connection);
}
