package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository implements IRepository<Order> {

    private static IRepository<Order> instance;

    private List<Order> orders = new ArrayList<>();

    private OrderRepository() {

    }

    public static IRepository<Order> getInstance() {
        if (instance == null) {
            instance = new OrderRepository();
        }
        return instance;
    }

    @Override
    public List<Order> findAll() {
        return List.copyOf(orders);
    }

    @Override
    public Order findById(Long id) {
        if (id != null) {
            for (Order order : orders) {
                if (order.getId().equals(id)) {
                    return order;
                }
            }
        }
        return null;
    }

    @Override
    public void add(Order entity) {
        if (entity != null && !orders.contains(entity)) {
            entity.setId(IdGenerator.getInstance().getNextOrderIdNumber());
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
