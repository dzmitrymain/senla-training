package com.senla.training.yeutukhovich.bookstore.util.comparator.order;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.AvailabilityBookComparator;

import java.util.Comparator;

public class CompletionDateOrderComparator implements Comparator<Order> {

    private static CompletionDateOrderComparator instance;

    public static CompletionDateOrderComparator getInstance() {
        if (instance == null) {
            instance = new CompletionDateOrderComparator();
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
            if (o1.getCompletionDate() == null && o2.getCompletionDate() == null) {
                return 0;
            } else if (o1.getCompletionDate() == null) {
                return 1;
            } else if (o2.getCompletionDate() == null) {
                return -1;
            } else {
                return o2.getCompletionDate().compareTo(o1.getCompletionDate());
            }
        }
    }
}
