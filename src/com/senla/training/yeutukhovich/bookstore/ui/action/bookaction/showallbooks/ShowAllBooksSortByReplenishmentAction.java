package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.ReplenishmentDateBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllBooksSortByReplenishmentAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();
        EntityPrinter.printEntities(bookService.findAllBooks(ReplenishmentDateBookComparator.getInstance()));
    }
}
