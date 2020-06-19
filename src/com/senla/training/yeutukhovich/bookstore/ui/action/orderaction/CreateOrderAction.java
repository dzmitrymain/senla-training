package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CreateOrderAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
        Long id = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_CUSTOMER_DATA.getMessage());
        String customerData = InputReader.readInputString();

        if (id != null && customerData != null) {
            Order order = orderController.createOrder(id, customerData);
            if (order != null) {
                System.out.println(MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage());
                if (order.getBook() != null && !order.getBook().isAvailable()) {
                    System.out.println(MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage());
                }
            } else {
                System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_CREATED.getMessage());
            }
        }
    }
}
