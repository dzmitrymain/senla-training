package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
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
            LOGGER.info(LoggerConstant.CREATE_REQUEST_SUCCESS.getMessage(), bookId);
            return MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.CREATE_REQUEST_FAIL.getMessage(), bookId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByBookTitle() {
        try {
            String result = requestService.findSortedAllRequestsByBookTitle().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_BOOK_TITLE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByIsActive() {
        try {
            String result = requestService.findSortedAllRequestsByIsActive().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_IS_ACTIVE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllRequestsByRequesterData() {
        try {
            String result = requestService.findSortedAllRequestsByRequesterData().stream()
                    .map(Request::toString)
                    .collect(Collectors.joining(REQUEST_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_REQUESTER_DATA.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllRequests(String fileName) {
        try {
            String result = MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + requestService.exportAllRequests(fileName);
            LOGGER.info(LoggerConstant.EXPORT_ALL_REQUESTS.getMessage(), fileName);
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportRequest(Long requestId, String fileName) {
        try {
            requestService.exportRequest(requestId, fileName);
            LOGGER.info(LoggerConstant.EXPORT_REQUEST_SUCCESS.getMessage(), requestId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.EXPORT_REQUEST_FAIL.getMessage(), requestId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importRequests(String fileName) {
        try {
            String result = MessageConstant.IMPORTED_ENTITIES.getMessage()
                    + requestService.importRequests(fileName);
            LOGGER.info(LoggerConstant.IMPORT_REQUESTS_SUCCESS.getMessage(), fileName);
            return result;
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.IMPORT_REQUESTS_FAIL.getMessage(), e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
