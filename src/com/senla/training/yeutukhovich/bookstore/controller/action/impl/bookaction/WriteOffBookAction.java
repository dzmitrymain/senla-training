package com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class WriteOffBookAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID);
        Long id = InputReader.readInputLong();

        if (id != null) {
            if (bookService.writeOffBook(id)) {
                System.out.println(MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF);
            } else {
                System.out.println(MessageConstant.BOOK_HAS_NOT_BEEN_WRITTEN_OFF);
            }
        }

    }
}
