package com.senla.training.yeutukhovich.bookstore.service.order;

import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {

    CreationOrderResult createOrder(Long bookId, String customerData);

    boolean cancelOrder(Long orderId);

    boolean completeOrder(Long orderId);

    List<String> findSortedAllOrdersByCompletionDate();

    List<String> findSortedAllOrdersByPrice();

    List<String> findSortedAllOrdersByState();

    List<String> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetails showOrderDetails(Long orderId);

    int exportAllOrders(String fileName);

    boolean exportOrder(Long id, String fileName);

    int importOrders(String fileName);
}