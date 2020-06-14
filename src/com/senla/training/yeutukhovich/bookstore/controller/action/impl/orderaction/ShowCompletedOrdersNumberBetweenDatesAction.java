package com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction;

import com.senla.training.yeutukhovich.bookstore.controller.action.Action;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Date;

public class ShowCompletedOrdersNumberBetweenDatesAction implements Action {

    @Override
    public void execute() {
        OrderService orderService= OrderServiceImpl.getInstance();

        System.out.println("Please enter an earliest date bound in format \"yyyy-MM-dd\": ");
        Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        System.out.println("Please enter a latest date bound in format \"yyyy-MM-dd\": ");
        Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

        if (firstDate != null && secondDate != null) {
            System.out.println("Completed orders number: "+orderService.calculateCompletedOrdersNumberBetweenDates(firstDate,secondDate));
        }
    }
}
