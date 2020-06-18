package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class CompleteOrderAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
        Long orderId = InputReader.readInputLong();

        if (orderId != null) {
            if (orderService.completeOrder(orderId)) {
                System.out.println(MessageConstant.ORDER_HAS_BEEN_COMPLETED.getMessage());
            } else {
                System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_COMPLETED.getMessage());
            }
        }
    }
}
