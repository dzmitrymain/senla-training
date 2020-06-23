package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.EntityExchanger;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsexchanger.RequestCvsExchanger;
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

    public Request findById(Long requestId) {
        return requestService.findById(requestId);
    }

    public int importRequests(String fileName) {
        EntityExchanger<Request> requestExchanger = RequestCvsExchanger.getInstance();
        return requestExchanger.importEntities(fileName);
    }

    public void exportRequests(List<Request> requests, String fileName) {
        EntityExchanger<Request> requestExchanger = RequestCvsExchanger.getInstance();
        requestExchanger.exportEntities(requests, fileName);
    }
}
