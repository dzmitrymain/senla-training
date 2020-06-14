package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;

public class CreateOrderAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println("Please, enter book id: ");
        Long id = InputReader.readInputLong();

        System.out.println("Please, enter customer data: ");
        String customerData = InputReader.readInputString();

        if (id != null && customerData!=null) {
            orderService.createOrder(id, customerData);
        }
    }
}
