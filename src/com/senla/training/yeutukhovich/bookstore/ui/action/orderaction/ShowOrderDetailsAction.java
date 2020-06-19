package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ShowOrderDetailsAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();

        System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
        Long id = InputReader.readInputLong();

        if (id != null) {
            OrderDetails orderDetails = orderController.showOrderDetails(id);
            if (orderDetails != null) {
                System.out.println(orderDetails);
            } else {
                System.out.println(MessageConstant.ORDER_DETAILS_WAS_NOT_FOUND.getMessage());
            }
        }
    }
}
