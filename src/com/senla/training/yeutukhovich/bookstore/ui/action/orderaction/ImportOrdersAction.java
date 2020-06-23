package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ImportOrdersAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int importedOrdersNumber = 0;
        if (fileName != null) {
            importedOrdersNumber = orderController.importOrders(fileName);
        }

        System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedOrdersNumber);
    }
}
