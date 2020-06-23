package com.senla.training.yeutukhovich.bookstore.util.comparator.request;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.Comparator;

public class IdRequestComparator implements Comparator<Request> {

    @Override
    public int compare(Request o1, Request o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return o1.getId().compareTo(o2.getId());
        }
    }
}
