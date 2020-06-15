package com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.PriceBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllBooksSortByPriceAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();
        EntityPrinter.printEntities(bookService.findAllBooks(PriceBookComparator.getInstance()));
    }
}
