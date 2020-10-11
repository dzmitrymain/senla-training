package com.senla.training.yeutukhovich.bookstore.controller.config;

import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public BookService bookService() {
        return Mockito.mock(BookService.class);
    }

    @Bean
    public OrderService orderService() {
        return Mockito.mock(OrderService.class);
    }

    @Bean
    public RequestService requestService() {
        return Mockito.mock(RequestService.class);
    }
}
