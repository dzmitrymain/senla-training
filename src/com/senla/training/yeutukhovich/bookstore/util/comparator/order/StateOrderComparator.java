package com.senla.training.yeutukhovich.bookstore.util.comparator.order;

import com.senla.training.yeutukhovich.bookstore.domain.Order;

import java.util.Comparator;

public class StateOrderComparator implements Comparator<Order> {

    private static StateOrderComparator instance;

    public static StateOrderComparator getInstance() {
        if (instance == null) {
            instance = new StateOrderComparator();
        }
        return instance;
    }

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
