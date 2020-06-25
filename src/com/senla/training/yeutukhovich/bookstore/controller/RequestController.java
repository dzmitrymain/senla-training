package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestServiceImpl;

import java.util.List;

public class RequestController {

    private static RequestController instance;

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

    public List<String> findSortedAllRequestsByBookTitle() {
        return requestService.findSortedAllRequestsByBookTitle();
    }

    public List<String> findSortedAllRequestsByIsActive() {
        return requestService.findSortedAllRequestsByIsActive();
    }

    public List<String> findSortedAllRequestsByRequesterData() {
        return requestService.findSortedAllRequestsByRequesterData();
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
