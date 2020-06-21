package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportOrderAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();

        System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
        Long orderId = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        if (orderId != null && fileName != null) {
            Order order = orderController.findById(orderId);
            if (order != null) {
                orderController.exportOrders(List.of(order), fileName);
                System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
            } else {
                System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
            }
        } else {
            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
        }
    }
}
