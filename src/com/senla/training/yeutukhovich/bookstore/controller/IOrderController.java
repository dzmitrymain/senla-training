package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public interface IOrderController {

    Order createOrder(Long bookId, String customerData);

    boolean cancelOrder(Long orderId);

    boolean completeOrder(Long orderId);

    List<Order> findAllOrders(Comparator<Order> orderComparator);

    List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetails showOrderDetails(Long orderId);
}
