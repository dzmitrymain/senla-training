package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders;

import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.OrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllOrdersSortByStateAction implements Action {

    @Override
    public void execute() {
        OrderController orderController = OrderController.getInstance();
        EntityPrinter.printEntities(orderController.findAllOrders(OrderComparator.STATE.getComparator()));
    }
}
