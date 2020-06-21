package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportBookAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();

        System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
        Long bookId = InputReader.readInputLong();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        if (bookId != null && fileName != null) {
            Book book = bookController.findById(bookId);
            if (book != null) {
                bookController.exportBooks(List.of(book), fileName);
                System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
            } else {
                System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
            }
        } else {
            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
        }
    }
}
