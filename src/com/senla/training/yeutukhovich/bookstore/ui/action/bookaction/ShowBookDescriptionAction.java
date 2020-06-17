package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ShowBookDescriptionAction implements Action {

    @Override
    public void execute() {

        BookService bookService = BookServiceImpl.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID);
        Long id = InputReader.readInputLong();

        if (id != null) {
            BookDescription bookDescription = bookService.showBookDescription(id);
            if (bookDescription != null) {
                System.out.println(bookDescription);
            } else {
                System.out.println(MessageConstant.BOOK_DESCRIPTION_WAS_NOT_FOUND);
            }
        }
    }
}
