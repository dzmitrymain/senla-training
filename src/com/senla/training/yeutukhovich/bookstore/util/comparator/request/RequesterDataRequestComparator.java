package com.senla.training.yeutukhovich.bookstore.util.comparator.request;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.StateOrderComparator;

import java.util.Comparator;

public class RequesterDataRequestComparator implements Comparator<Request> {

    private static RequesterDataRequestComparator instance;

    public static RequesterDataRequestComparator getInstance() {
        if (instance == null) {
            instance = new RequesterDataRequestComparator();
        }
        return instance;
    }

    @Override
    public int compare(Request o1, Request o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return o1.getRequesterData().compareTo(o2.getRequesterData());
        }
    }
}
