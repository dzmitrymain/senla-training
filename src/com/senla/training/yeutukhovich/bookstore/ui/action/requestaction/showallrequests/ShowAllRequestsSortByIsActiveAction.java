package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.showallrequests;

import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.RequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllRequestsSortByIsActiveAction implements Action {

    @Override
    public void execute() {
        RequestController requestController = RequestController.getInstance();
        EntityPrinter.printEntities(requestController.findAllRequests(RequestComparator.IS_ACTIVE.getComparator()));
    }
}
