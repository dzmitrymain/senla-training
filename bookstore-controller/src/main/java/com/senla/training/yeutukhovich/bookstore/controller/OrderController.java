package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{id}")
    public OrderDto createOrder(@PathVariable("id") Long bookId, @RequestParam String customerData) {
        return orderService.createOrder(bookId, customerData);
    }

    @PostMapping("/cancel/{id}")
    public OrderDto cancelOrder(@PathVariable("id") Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/complete/{id}")
    public OrderDto completeOrder(@PathVariable("id") Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @GetMapping("/allOrdersByCompletionDate")
    public List<OrderDto> findSortedAllOrdersByCompletionDate() {
        return orderService.findSortedAllOrdersByCompletionDate();
    }

    @GetMapping("/allOrdersByPrice")
    public List<OrderDto> findSortedAllOrdersByPrice() {
        return orderService.findSortedAllOrdersByPrice();
    }

    @GetMapping("/allOrdersByState")
    public List<OrderDto> findSortedAllOrdersByState() {
        return orderService.findSortedAllOrdersByState();
    }

    @GetMapping("/completedBetweenDates")
    public List<OrderDto> findCompletedOrdersBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return orderService.findCompletedOrdersBetweenDates(startDate, endDate);
    }

    @GetMapping("/profitBetweenDates")
    public Map calculateProfitBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return Collections.singletonMap("profit", orderService.calculateProfitBetweenDates(startDate, endDate));
    }

    @GetMapping("/ordersNumberBetweenDates")
    public Map calculateCompletedOrdersNumberBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return Collections.singletonMap("ordersNumber",
                orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate));
    }

    @GetMapping("/details/{id}")
    public OrderDetailsDto showOrderDetails(@PathVariable("id") Long orderId) {
        return orderService.showOrderDetails(orderId);
    }

    @PostMapping("/import")
    public List<OrderDto> importOrders(@RequestParam String fileName) {
        return orderService.importOrders(fileName);
    }

    @GetMapping("/exportAll")
    public List<OrderDto> exportAllOrders(@RequestParam String fileName) {
        return orderService.exportAllOrders(fileName);
    }

    @GetMapping("/export/{id}")
    public OrderDto exportOrder(@PathVariable("id") Long orderId, @RequestParam String fileName) {
        return orderService.exportOrder(orderId, fileName);
    }
}
