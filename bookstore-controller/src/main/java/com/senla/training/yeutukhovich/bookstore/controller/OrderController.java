package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/state")
    public OrderDto changeState(@PathVariable("id") Long orderId, @RequestBody OrderDto orderDto) {
        return orderService.updateState(orderId, orderDto);
    }

    @GetMapping
    public List<OrderDto> findSortedAllOrders(@RequestParam("sort") String sortParam) {
        return orderService.findSortedAllOrders(sortParam);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import")
    public List<OrderDto> importOrders(@RequestParam String fileName) {
        return orderService.importOrders(fileName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/export")
    public List<OrderDto> exportAllOrders(@RequestParam String fileName) {
        return orderService.exportAllOrders(fileName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/export")
    public OrderDto exportOrder(@PathVariable("id") Long orderId, @RequestParam String fileName) {
        return orderService.exportOrder(orderId, fileName);
    }
}
