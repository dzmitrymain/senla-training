package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.showallbooks;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.comparator.BookComparator;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.List;

public class ExportAllBooksAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();
        List<Book> books = bookController.findAllBooks(BookComparator.ID.getComparator());

        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = InputReader.readInputString();

        int exportedBooksNumber = 0;

        if (books != null && fileName != null) {
            exportedBooksNumber = books.size();
            if (!books.isEmpty()) {
                bookController.exportBooks(books, fileName);
            }
        }
        System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedBooksNumber);
    }
}
