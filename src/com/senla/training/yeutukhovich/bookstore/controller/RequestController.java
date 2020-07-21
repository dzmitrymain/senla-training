package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.stream.Collectors;

@Singleton
public class RequestController {

    private static final String REQUEST_DELIMITER = "\n";

    @Autowired
    private RequestService requestService;

    private RequestController() {

    }

    public String createRequest(Long bookId, String requesterData) {
        try {
            requestService.createRequest(bookId, requesterData);
            return MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        }
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

    public String exportAllRequests(String fileName) {
        try {
            return MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + requestService.exportAllRequests(fileName);
        } catch (InternalException e) {
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportRequest(Long requestId, String fileName) {
        try {
            requestService.exportRequest(requestId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importRequests(String fileName) {
        try {
            return MessageConstant.IMPORTED_ENTITIES.getMessage()
                    + requestService.importRequests(fileName);
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
