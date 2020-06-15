package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CompleteOrderAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_ORDER_ID);
        Long orderId = InputReader.readInputLong();

        if (orderId != null) {
            if (orderService.completeOrder(orderId)) {
                System.out.println(MessageConstant.ORDER_HAS_BEEN_COMPLETED);
            } else {
                System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_COMPLETED);
            }
        }
    }
}
