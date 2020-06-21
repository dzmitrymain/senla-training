package com.senla.training.yeutukhovich.bookstore.util.comparator;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.CompletionDateOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.IdOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.PriceOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.StateOrderComparator;

import java.util.Comparator;

public enum OrderComparator {

    ID(new IdOrderComparator()),
    COMPLETION_DATE(new CompletionDateOrderComparator()),
    PRICE(new PriceOrderComparator()),
    STATE(new StateOrderComparator());

    private Comparator<Order> comparator;

    OrderComparator(Comparator<Order> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Order> getComparator() {
        return comparator;
    }
}
