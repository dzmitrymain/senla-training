package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ReplenishBookAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID);
        Long id = InputReader.readInputLong();

        if (id != null) {
            if (bookController.replenishBook(id)) {
                System.out.println(MessageConstant.BOOK_HAS_BEEN_REPLENISHED);
            } else {
                System.out.println(MessageConstant.BOOK_HAS_NOT_BEEN_REPLENISHED);
            }
        }
    }
}
