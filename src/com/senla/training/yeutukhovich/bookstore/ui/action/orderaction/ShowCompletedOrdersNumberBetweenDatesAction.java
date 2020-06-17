package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Date;

public class ShowCompletedOrdersNumberBetweenDatesAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD);
        Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD);
        Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        if (firstDate != null && secondDate != null) {
            System.out.println(MessageConstant.COMPLETED_ORDERS_NUMBER +
                    orderService.calculateCompletedOrdersNumberBetweenDates(firstDate, secondDate));
        }
    }
}
