package com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.showallrequests;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.requestservice.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.requestservice.RequestServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.request.IsActiveRequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllRequestsSortByIsActiveAction implements Action {

    @Override
    public void execute() {
        RequestService requestService = RequestServiceImpl.getInstance();
        EntityPrinter.printEntities(requestService.findAllRequests(IsActiveRequestComparator.getInstance()));
    }
}
