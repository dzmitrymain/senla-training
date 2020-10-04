package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {

    Order createOrder(Long bookId, String customerData);

    Order cancelOrder(Long orderId);

    Order completeOrder(Long orderId);

    List<Order> findSortedAllOrdersByCompletionDate();

    List<Order> findSortedAllOrdersByPrice();

    List<Order> findSortedAllOrdersByState();

    List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetailsDto showOrderDetails(Long orderId);

    List<Order> exportAllOrders(String fileName);

    Order exportOrder(Long id, String fileName);

    List<Order> importOrders(String fileName);
}
