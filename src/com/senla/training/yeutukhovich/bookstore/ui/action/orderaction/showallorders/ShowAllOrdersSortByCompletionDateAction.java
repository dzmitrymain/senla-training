package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.CompletionDateOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllOrdersSortByCompletionDateAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();
        EntityPrinter.printEntities(orderService.findAllOrders(CompletionDateOrderComparator.getInstance()));
    }
}
