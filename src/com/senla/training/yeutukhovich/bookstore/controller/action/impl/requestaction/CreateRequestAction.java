package com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.impl.RequestServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CreateRequestAction implements Action {

    @Override
    public void execute() {
        RequestService requestService = RequestServiceImpl.getInstance();

        System.out.println("Please, enter book id: ");
        Long bookId = InputReader.readInputLong();

        System.out.println("Please, enter requester data: ");
        String requesterData = InputReader.readInputString();

        if (bookId != null && requesterData != null) {
            requestService.createRequest(bookId, requesterData);
        }
    }
}
