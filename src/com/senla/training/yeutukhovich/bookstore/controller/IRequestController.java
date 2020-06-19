package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.Comparator;
import java.util.List;

public interface IRequestController {

    Request createRequest(Long bookId, String requesterData);

    List<Request> findAllRequests(Comparator<Request> requestComparator);
}