package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class ImportBooksAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int importedBooksNumber = 0;
        if (fileName != null) {
            importedBooksNumber = bookController.importBooks(fileName);
        }

        System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedBooksNumber);
    }
}
