package com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.impl.RequestServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CreateRequestAction implements Action {

    @Override
    public void execute() {
        RequestService requestService = RequestServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID);
        Long bookId = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_REQUESTER_DATA);
        String requesterData = InputReader.readInputString();

        if (bookId != null && requesterData != null) {
            Request request = requestService.createRequest(bookId, requesterData);
            if (request != null) {
                System.out.println(MessageConstant.REQUEST_HAS_BEEN_CREATED);
            } else {
                System.out.println(MessageConstant.REQUEST_HAS_NOT_BEEN_CREATED);
            }
        }
    }
}
