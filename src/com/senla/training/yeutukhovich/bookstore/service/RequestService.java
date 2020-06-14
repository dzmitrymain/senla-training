package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.Comparator;

public interface RequestService {

    Request createRequest(Long bookId, String requesterData);
    Request[] findAllRequests(Comparator<Request> requestComparator);
}
