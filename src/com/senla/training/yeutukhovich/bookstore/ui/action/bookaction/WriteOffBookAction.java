package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class WriteOffBookAction implements Action {

    @Override
    public void execute() {
        BookService bookService = BookServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
        Long id = InputReader.readInputLong();

        if (id != null) {
            if (bookService.writeOffBook(id)) {
                System.out.println(MessageConstant.BOOK_HAS_BEEN_WRITTEN_OFF.getMessage());
            } else {
                System.out.println(MessageConstant.BOOK_HAS_NOT_BEEN_WRITTEN_OFF.getMessage());
            }
        }
    }
}
