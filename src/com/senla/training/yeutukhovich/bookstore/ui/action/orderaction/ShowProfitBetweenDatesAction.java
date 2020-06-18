package com.senla.training.yeutukhovich.bookstore.ui.action.orderaction;

import com.senla.training.yeutukhovich.bookstore.ui.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Date;

public class ShowProfitBetweenDatesAction implements Action {

    @Override
    public void execute() {
        OrderService orderService = OrderServiceImpl.getInstance();

        System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
        Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
        Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        if (firstDate != null && secondDate != null) {
            System.out.println(MessageConstant.PROFIT.getMessage() + orderService.calculateProfitBetweenDates(firstDate, secondDate));
        }
    }
}
