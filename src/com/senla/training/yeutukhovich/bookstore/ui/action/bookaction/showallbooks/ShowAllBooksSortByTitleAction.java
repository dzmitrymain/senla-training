package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.bookservice.BookService;
import com.senla.training.yeutukhovich.bookstore.service.bookservice.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.TitleBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;

public class ShowAllBooksSortByTitleAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();
        EntityPrinter.printEntities(bookService.findAllBooks(TitleBookComparator.getInstance()));
    }
}
