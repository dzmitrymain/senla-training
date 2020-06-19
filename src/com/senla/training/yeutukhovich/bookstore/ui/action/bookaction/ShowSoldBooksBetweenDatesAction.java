package com.senla.training.yeutukhovich.bookstore.ui.action.bookaction;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Date;

public class ShowSoldBooksBetweenDatesAction implements Action {

    @Override
    public void execute() {
        BookController bookController = BookController.getInstance();

        System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
        Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
        Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        if (firstDate != null && secondDate != null) {
            EntityPrinter.printEntities(bookController.findSoldBooksBetweenDates(firstDate, secondDate));
        }
    }
}
