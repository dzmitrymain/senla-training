package com.senla.training.yeutukhovich.bookstore.service.request;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.List;

public interface RequestService {

    Long createRequest(Long bookId, String requesterData);

    List<Request> findSortedAllRequestsByBookTitle();

    List<Request> findSortedAllRequestsByIsActive();

    List<Request> findSortedAllRequestsByRequesterData();

    int exportAllRequests(String fileName);

    boolean exportRequest(Long requestId, String fileName);

    int importRequests(String fileName);

    void serializeRequests();

    void deserializeRequests();
}
