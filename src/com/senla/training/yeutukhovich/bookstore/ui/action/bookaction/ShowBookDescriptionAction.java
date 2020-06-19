package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ShowBookDescriptionAction implements Action {

    @Override
    public void execute() {

        BookController bookController = BookController.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
        Long id = InputReader.readInputLong();

        if (id != null) {
            BookDescription bookDescription = bookController.showBookDescription(id);
            if (bookDescription != null) {
                System.out.println(bookDescription);
            } else {
                System.out.println(MessageConstant.BOOK_DESCRIPTION_WAS_NOT_FOUND.getMessage());
            }
        }
    }
}
