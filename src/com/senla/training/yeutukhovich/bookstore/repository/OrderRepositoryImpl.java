package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class OrderRepositoryImpl implements OrderRepository {

    @Autowired
    private IdGenerator idGenerator;
    private List<Order> orders = new ArrayList<>();

    private OrderRepositoryImpl() {

    }

    @Override
    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orders.stream()
                .filter(order -> order.getId().equals(id))
                .findFirst();
    }

    @Override
    public void add(Order entity) {
        if (entity != null && !orders.contains(entity)) {
            entity.setId(idGenerator.getNextOrderIdNumber());
            orders.add(entity);
        }
    }

    @Override
    public void update(Order entity) {
        if (entity != null) {
            int index = orders.indexOf(entity);
            if (index != -1) {
                orders.set(index, entity);
            }
        }
    }
}
