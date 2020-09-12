package com.senla.training.yeutukhovich.bookstore.model.dao.request;

import com.senla.training.yeutukhovich.bookstore.model.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;

import java.util.List;

public interface RequestDao extends GenericDao<Request, Long> {

    Long closeRequestsByBookId(Long bookId);

    List<Request> findSortedAllRequestsByBookTitle();

    List<Request> findSortedAllRequestsByIsActive();

    List<Request> findSortedAllRequestsByRequesterData();
}
