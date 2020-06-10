package com.senla.training.yeutukhovich.bookstore.domain.comparator.order;

import com.senla.training.yeutukhovich.bookstore.domain.Order;

import java.util.Comparator;

public class StateOrderComparator implements Comparator<Order> {

    @Override
    public int compare(Order o1, Order o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return o1.getState().compareTo(o2.getState());
        }
    }
}
