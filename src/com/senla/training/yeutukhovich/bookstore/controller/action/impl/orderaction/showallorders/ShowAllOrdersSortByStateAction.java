package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.showallorders;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.StateOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.ResponsePrinter;

public class ShowAllOrdersSortByStateAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();
        ResponsePrinter.printEntities(orderService.findAllOrders(StateOrderComparator.getInstance()));
    }
}
