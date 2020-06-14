package com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction.showallrequests;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;
import com.senla.training.yeutukhovich.bookstore.service.impl.RequestServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.request.IsActiveRequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.ResponsePrinter;

public class ShowAllRequestsSortByIsActiveAction implements Action {

    @Override
    public void execute() {
        RequestService requestService = RequestServiceImpl.getInstance();
        ResponsePrinter.printEntities(requestService.findAllRequests(IsActiveRequestComparator.getInstance()));
    }
}
