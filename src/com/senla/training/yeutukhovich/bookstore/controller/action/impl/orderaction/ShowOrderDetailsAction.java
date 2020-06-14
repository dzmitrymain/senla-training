package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ShowOrderDetailsAction implements Action {

    @Override
    public void execute() {

        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println("Please, enter order id: ");
        Long id = InputReader.readInputLong();

        OrderDetails orderDetails = null;
        if (id != null) {
            orderDetails = orderService.showOrderDetails(id);
        }
        if (orderDetails != null) {
            System.out.println(orderDetails);
        }
    }
}
