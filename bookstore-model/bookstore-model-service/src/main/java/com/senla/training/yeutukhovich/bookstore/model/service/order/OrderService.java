package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {

    CreationOrderResult createOrder(Long bookId, String customerData);

    void cancelOrder(Long orderId);

    void completeOrder(Long orderId);

    List<Order> findSortedAllOrdersByCompletionDate();

    List<Order> findSortedAllOrdersByPrice();

    List<Order> findSortedAllOrdersByState();

    List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetails showOrderDetails(Long orderId);

    int exportAllOrders(String fileName);

    void exportOrder(Long id, String fileName);

    int importOrders(String fileName);
}
