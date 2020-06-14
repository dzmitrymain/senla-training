package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public interface OrderService {

    Order createOrder(Long bookId, String customerData);

    void cancelOrder(Long orderId);

    void completeOrder(Long orderId);

    Order[] findAllOrders(Comparator<Order> orderComparator);

    Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetails showOrderDetails(Long orderId);
}
