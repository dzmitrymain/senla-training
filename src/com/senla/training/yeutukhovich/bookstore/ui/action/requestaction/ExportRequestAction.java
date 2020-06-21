package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportRequestAction implements Action {

    @Override
    public void execute() {
        RequestController requestController = RequestController.getInstance();

        System.out.println(MessageConstant.ENTER_REQUEST_ID.getMessage());
        Long requestId = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        if (requestId != null && fileName != null) {
            Request request = requestController.findById(requestId);
            if (request != null) {
                requestController.exportRequests(List.of(request), fileName);
                System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
            } else {
                System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
            }
        } else {
            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
        }
    }
}
