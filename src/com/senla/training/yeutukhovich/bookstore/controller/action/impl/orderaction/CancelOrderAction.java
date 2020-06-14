package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;

public class CancelOrderAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println("Please, enter order id: ");
        Long orderId = InputReader.readInputLong();

        if (orderId != null) {
            orderService.cancelOrder(orderId);
        }
    }
}
