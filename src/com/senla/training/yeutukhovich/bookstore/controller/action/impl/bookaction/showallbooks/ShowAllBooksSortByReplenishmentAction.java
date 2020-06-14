package com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.ReplenishmentDateBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.ResponsePrinter;

public class ShowAllBooksSortByReplenishmentAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();
        ResponsePrinter.printEntities(bookService.findAllBooks(ReplenishmentDateBookComparator.getInstance()));
    }
}
