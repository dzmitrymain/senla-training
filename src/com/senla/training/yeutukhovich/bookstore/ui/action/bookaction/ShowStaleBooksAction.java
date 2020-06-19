package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowStaleBooksAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();
        EntityPrinter.printEntities(bookController.findStaleBooks());
    }
}
