package com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

import java.util.Arrays;

public class ShowStaleBooksAction implements Action {

    @Override
    public void execute() {

        BookService bookService = BookServiceImpl.getInstance();
        EntityPrinter.printEntity(Arrays.asList(bookService.findStaleBooks()));
    }
}
