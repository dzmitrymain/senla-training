package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CreateRequestAction implements Action {

    @Override
    public void execute() {
        RequestController requestController = RequestController.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
        Long bookId = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_REQUESTER_DATA.getMessage());
        String requesterData = InputReader.readInputString();

        if (bookId != null && requesterData != null) {
            Request request = requestController.createRequest(bookId, requesterData);
            if (request != null) {
                System.out.println(MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage());
            } else {
                System.out.println(MessageConstant.REQUEST_HAS_NOT_BEEN_CREATED.getMessage());
            }
        }
    }
}
