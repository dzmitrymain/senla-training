package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.OrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportAllOrdersAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();
        List<Order> orders = orderController.findAllOrders(OrderComparator.ID.getComparator());

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int exportedOrdersNumber = 0;

        if (orders != null && fileName != null) {
            exportedOrdersNumber = orders.size();
            if (!orders.isEmpty()) {
                orderController.exportOrders(orders, fileName);
            }
        }
        System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedOrdersNumber);
    }
}
