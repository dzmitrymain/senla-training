package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.showallorders;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.CompletionDateOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

import java.util.Arrays;

public class ShowAllOrdersSortByCompletionDateAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();
        Order[] orders = orderService.findAllOrders(CompletionDateOrderComparator.getInstance());
        EntityPrinter.printEntity(Arrays.asList(orders));
    }
}
