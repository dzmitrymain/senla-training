package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.RequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportAllRequestsAction implements Action {

    @Override
    public void execute() {
        RequestController requestController = RequestController.getInstance();
        List<Request> requests = requestController.findAllRequests(RequestComparator.ID.getComparator());

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int exportedRequestsNumber = 0;

        if (requests != null && fileName != null) {
            exportedRequestsNumber = requests.size();
            if (!requests.isEmpty()) {
                requestController.exportRequests(requests, fileName);
            }
        }
        System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedRequestsNumber);
    }
}
