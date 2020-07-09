package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.stream.Collectors;

@Singleton
public class RequestController {

    private static final String REQUEST_DELIMITER = "\n";

    @Autowired
    private RequestService requestService;

    private RequestController(){

    }

    public Long createRequest(Long bookId, String requesterData) {
        return requestService.createRequest(bookId, requesterData);
    }

    public String findSortedAllRequestsByBookTitle() {
        return requestService.findSortedAllRequestsByBookTitle().stream()
                .map(Request::toString)
                .collect(Collectors.joining(REQUEST_DELIMITER));
    }

    public String findSortedAllRequestsByIsActive() {
        return requestService.findSortedAllRequestsByIsActive().stream()
                .map(Request::toString)
                .collect(Collectors.joining(REQUEST_DELIMITER));
    }

    public String findSortedAllRequestsByRequesterData() {
        return requestService.findSortedAllRequestsByRequesterData().stream()
                .map(Request::toString)
                .collect(Collectors.joining(REQUEST_DELIMITER));
    }

    public int exportAllRequests(String fileName) {
        return requestService.exportAllRequests(fileName);
    }

    public boolean exportRequest(Long requestId, String fileName) {
        return requestService.exportRequest(requestId, fileName);
    }

    public int importRequests(String fileName) {
        return requestService.importRequests(fileName);
    }
}
