package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public interface OrderService {

    Order createOrder(Book book, String customerData);

    void cancelOrder(Order order);

    void completeOrder(Order order);

    Order[] findAllOrders(Comparator<Order> orderComparator);

    Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate, Comparator<Order> orderComparator);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetails showOrderDetails(Order order);
}
