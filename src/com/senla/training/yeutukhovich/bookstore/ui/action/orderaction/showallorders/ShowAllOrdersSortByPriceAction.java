package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.PriceOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllOrdersSortByPriceAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();
        EntityPrinter.printEntities(orderService.findAllOrders(PriceOrderComparator.getInstance()));
    }
}
