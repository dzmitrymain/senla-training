package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction;

import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ImportRequestsAction implements Action {

    @Override
    public void execute() {
        RequestController requestController = RequestController.getInstance();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int importedRequestsNumber = 0;
        if (fileName != null) {
            importedRequestsNumber = requestController.importRequests(fileName);
        }

        System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedRequestsNumber);
    }
}
