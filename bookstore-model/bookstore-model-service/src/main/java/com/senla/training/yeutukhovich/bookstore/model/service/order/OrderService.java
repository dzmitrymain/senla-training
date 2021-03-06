package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {

    OrderDto createOrder(Long bookId, String customerData);

    OrderDto updateState(Long orderId, OrderDto orderDto);

    List<OrderDto> findSortedAllOrders(String sortParam);

    List<OrderDto> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    OrderDetailsDto showOrderDetails(Long orderId);

    List<OrderDto> exportAllOrders(String fileName);

    OrderDto exportOrder(Long id, String fileName);

    List<OrderDto> importOrders(String fileName);
}
