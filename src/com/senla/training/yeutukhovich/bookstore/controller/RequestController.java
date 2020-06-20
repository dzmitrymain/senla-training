package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.service.requestservice.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.requestservice.RequestServiceImpl;

import java.util.Comparator;
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


    public Request createRequest(Long bookId, String requesterData) {
        return requestService.createRequest(bookId, requesterData);
    }


    public List<Request> findAllRequests(Comparator<Request> requestComparator) {
        return requestService.findAllRequests(requestComparator);
    }
}
