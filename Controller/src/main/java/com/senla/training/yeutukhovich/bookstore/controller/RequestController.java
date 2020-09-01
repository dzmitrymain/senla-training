package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

@Singleton
public class RequestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestController.class);

    private static final String REQUEST_DELIMITER = "\n";

    @Autowired
    private RequestService requestService;

    public String createRequest(Long bookId, String requesterData) {
        try {
            requestService.createRequest(bookId, requesterData);
            return MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByBookTitle() {
        try {
            return requestService.findSortedAllRequestsByBookTitle().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByIsActive() {
        try {
            return requestService.findSortedAllRequestsByIsActive().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByRequesterData() {
        try {
            return requestService.findSortedAllRequestsByRequesterData().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllRequests(String fileName) {
        try {
            return MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + requestService.exportAllRequests(fileName);
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportRequest(Long requestId, String fileName) {
        try {
            requestService.exportRequest(requestId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importRequests(String fileName) {
        try {
            return MessageConstant.IMPORTED_ENTITIES.getMessage()
                    + requestService.importRequests(fileName);
        } catch (BusinessException e) {
            LOGGER.warn(e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
