package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Order;

import java.util.List;

public interface OrderRepository {

    List<Order> findAll();

    Order findById(Long id);

    void add(Order entity);

    void update(Order entity);
}
