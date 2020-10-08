package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long bookId, String customerData);

    OrderDto cancelOrder(Long orderId);

    OrderDto completeOrder(Long orderId);

    List<OrderDto> findSortedAllOrdersByCompletionDate();

    List<OrderDto> findSortedAllOrdersByPrice();

    List<OrderDto> findSortedAllOrdersByState();

    List<OrderDto> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetailsDto showOrderDetails(Long orderId);

    List<OrderDto> exportAllOrders(String fileName);

    OrderDto exportOrder(Long id, String fileName);

    List<OrderDto> importOrders(String fileName);
}
