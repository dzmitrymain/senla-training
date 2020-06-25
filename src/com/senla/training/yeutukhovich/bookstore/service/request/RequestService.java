package com.senla.training.yeutukhovich.bookstore.service.request;

import java.util.List;

public interface RequestService {

    Long createRequest(Long bookId, String requesterData);

    List<String> findSortedAllRequestsByBookTitle();

    List<String> findSortedAllRequestsByIsActive();

    List<String> findSortedAllRequestsByRequesterData();

    int exportAllRequests(String fileName);

    boolean exportRequest(Long requestId, String fileName);

    int importRequests(String fileName);
}
