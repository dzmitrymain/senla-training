package com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.controller.menu.util.reader.InputReader;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;

public class ShowBookDescriptionAction implements Action {

    @Override
    public void execute() {

        BookService bookService = BookServiceImpl.getInstance();

        System.out.println("Please, enter book id: ");
        Long id = InputReader.readInputLong();

        if (id != null) {
            System.out.println(bookService.showBookDescription(id));
        }
    }
}
