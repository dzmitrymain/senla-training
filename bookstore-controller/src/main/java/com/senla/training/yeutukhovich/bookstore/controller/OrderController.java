package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create/{bookId}&{customerData}")
    public OrderDto createOrder(@PathVariable("bookId") Long bookId, @PathVariable("customerData") String customerData) {
        return new OrderDto(orderService.createOrder(bookId, customerData));
    }

    @PutMapping("/cancel/{id}")
    public OrderDto cancelOrder(@PathVariable("id") Long orderId) {
        return new OrderDto(orderService.cancelOrder(orderId));
    }

    @PutMapping("/complete/{id}")
    public OrderDto completeOrder(@PathVariable("id") Long orderId) {
        return new OrderDto(orderService.completeOrder(orderId));
    }

    @GetMapping("allOrdersByCompletionDate")
    public List<OrderDto> findSortedAllOrdersByCompletionDate() {
        return orderService.findSortedAllOrdersByCompletionDate().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("allOrdersByPrice")
    public List<OrderDto> findSortedAllOrdersByPrice() {
        return orderService.findSortedAllOrdersByPrice().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("allOrdersByState")
    public List<OrderDto> findSortedAllOrdersByState() {
        return orderService.findSortedAllOrdersByState().stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("completedBetweenDates")
    public List<OrderDto> findCompletedOrdersBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return orderService.findCompletedOrdersBetweenDates(startDate, endDate).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("profitBetweenDates")
    public Map calculateProfitBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return Collections.singletonMap("profit", orderService.calculateProfitBetweenDates(startDate, endDate));
    }

    @GetMapping("ordersNumberBetweenDates")
    public Map calculateCompletedOrdersNumberBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return Collections.singletonMap("ordersNumber",
                orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate));
    }

    @GetMapping("/details/{id}")
    public OrderDetails showOrderDetails(@PathVariable("id") Long orderId) {
        return orderService.showOrderDetails(orderId);
    }

    @PutMapping("/import/{fileName}")
    public List<OrderDto> importOrders(@PathVariable("fileName") String fileName) {
        return orderService.importOrders(fileName).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/exportAll")
    public List<OrderDto> exportAllOrders(@RequestParam String fileName) {
        return orderService.exportAllOrders(fileName).stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/export")
    public OrderDto exportOrder(@RequestParam Long orderId, @RequestParam String fileName) {
        return new OrderDto(orderService.exportOrder(orderId, fileName));
    }
}
