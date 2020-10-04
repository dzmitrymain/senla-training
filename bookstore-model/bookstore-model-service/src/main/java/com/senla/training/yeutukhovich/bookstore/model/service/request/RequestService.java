package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.model.domain.Request;

import java.util.List;

public interface RequestService {

    Request createRequest(Long bookId, String requesterData);

    List<Request> findSortedAllRequestsByBookTitle();

    List<Request> findSortedAllRequestsByIsActive();

    List<Request> findSortedAllRequestsByRequesterData();

    List<Request> exportAllRequests(String fileName);

    Request exportRequest(Long requestId, String fileName);

    List<Request> importRequests(String fileName);
}
