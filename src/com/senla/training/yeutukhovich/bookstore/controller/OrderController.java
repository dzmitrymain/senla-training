package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.orderservice.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class OrderController implements IOrderController {

    private static OrderController instance;

    private OrderService orderService;

    private OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    public static OrderController getInstance() {
        if (instance == null) {
            instance = new OrderController(OrderServiceImpl.getInstance());
        }
        return instance;
    }

    @Override
    public Order createOrder(Long bookId, String customerData) {
        return orderService.createOrder(bookId, customerData);
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @Override
    public boolean completeOrder(Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @Override
    public List<Order> findAllOrders(Comparator<Order> orderComparator) {
        return orderService.findAllOrders(orderComparator);
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        return orderService.findCompletedOrdersBetweenDates(startDate, endDate);
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        return orderService.calculateProfitBetweenDates(startDate, endDate);
    }

    @Override
    public int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        return orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate);
    }

    @Override
    public OrderDetails showOrderDetails(Long orderId) {
        return orderService.showOrderDetails(orderId);
    }
}
