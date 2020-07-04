package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestServiceImpl;

import java.util.stream.Collectors;

public class RequestController {

    private static RequestController instance;

    private static final String REQUEST_DELIMITER = "\n";

    private RequestService requestService;

    private RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    public static RequestController getInstance() {
        if (instance == null) {
            instance = new RequestController(RequestServiceImpl.getInstance());
        }
        return instance;
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

    public void serializeRequests() {
        requestService.serializeRequests();
    }

    public void deserializeRequests() {
        requestService.deserializeRequests();
    }
}
