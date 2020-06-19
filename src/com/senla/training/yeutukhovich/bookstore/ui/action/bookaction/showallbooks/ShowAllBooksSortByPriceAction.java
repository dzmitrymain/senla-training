package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.BookComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllBooksSortByPriceAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();
        EntityPrinter.printEntities(bookController.findAllBooks(BookComparator.PRICE.getComparator()));
    }
}
