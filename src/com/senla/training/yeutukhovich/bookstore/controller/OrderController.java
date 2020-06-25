package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.order.OrderServiceImpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderController {

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

    public CreationOrderResult createOrder(Long bookId, String customerData) {
        return orderService.createOrder(bookId, customerData);
    }

    public boolean cancelOrder(Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    public boolean completeOrder(Long orderId) {
        return orderService.completeOrder(orderId);
    }

    public List<String> findSortedAllOrdersByCompletionDate() {
        return orderService.findSortedAllOrdersByCompletionDate();
    }

    public List<String> findSortedAllOrdersByPrice() {
        return orderService.findSortedAllOrdersByPrice();
    }

    public List<String> findSortedAllOrdersByState() {
        return orderService.findSortedAllOrdersByState();
    }

    public List<String> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        return orderService.findCompletedOrdersBetweenDates(startDate, endDate);
    }

    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        return orderService.calculateProfitBetweenDates(startDate, endDate);
    }

    public int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        return orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate);
    }

    public OrderDetails showOrderDetails(Long orderId) {
        return orderService.showOrderDetails(orderId);
    }

    public int importOrders(String fileName) {
        return orderService.importOrders(fileName);
    }

    public int exportAllOrders(String fileName) {
        return orderService.exportAllOrders(fileName);
    }

    public boolean exportOrder(Long orderId, String fileName) {
        return orderService.exportOrder(orderId, fileName);
    }
}
