package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.printer.EntityPrinter;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Date;

public class ShowCompletedOrdersBetweenDatesAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD);
        Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD);
        Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        if (firstDate != null && secondDate != null) {
            EntityPrinter.printEntities(orderService.findCompletedOrdersBetweenDates(firstDate, secondDate));
        }
    }
}
