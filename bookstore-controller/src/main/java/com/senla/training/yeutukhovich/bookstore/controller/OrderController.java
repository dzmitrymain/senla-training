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

    @PostMapping()
    public OrderDto createOrder(@RequestParam Long bookId, @RequestParam String customerData) {
        return orderService.createOrder(bookId, customerData);
    }

    @PostMapping("/{id}/cancel")
    public OrderDto cancelOrder(@PathVariable("id") Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/{id}/complete")
    public OrderDto completeOrder(@PathVariable("id") Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @GetMapping("/byCompletionDate")
    public List<OrderDto> findSortedAllOrdersByCompletionDate() {
        return orderService.findSortedAllOrdersByCompletionDate();
    }

    @GetMapping("/byPrice")
    public List<OrderDto> findSortedAllOrdersByPrice() {
        return orderService.findSortedAllOrdersByPrice();
    }

    @GetMapping("/byState")
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

    @GetMapping("/{id}/details")
    public OrderDetailsDto showOrderDetails(@PathVariable("id") Long orderId) {
        return orderService.showOrderDetails(orderId);
    }

    @PostMapping("/import")
    public List<OrderDto> importOrders(@RequestParam String fileName) {
        return orderService.importOrders(fileName);
    }

    @PostMapping("/export")
    public List<OrderDto> exportAllOrders(@RequestParam String fileName) {
        return orderService.exportAllOrders(fileName);
    }

    @PostMapping("/{id}/export")
    public OrderDto exportOrder(@PathVariable("id") Long orderId, @RequestParam String fileName) {
        return orderService.exportOrder(orderId, fileName);
    }
}
