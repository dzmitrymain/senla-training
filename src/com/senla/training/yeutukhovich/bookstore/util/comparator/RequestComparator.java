package com.senla.training.yeutukhovich.bookstore.util.comparator;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.comparator.request.BookIdRequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.request.IsActiveRequestComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.request.RequesterDataRequestComparator;

import java.util.Comparator;

public enum RequestComparator {

    BOOK_ID(new BookIdRequestComparator()),
    IS_ACTIVE(new IsActiveRequestComparator()),
    REQUESTER_DATA(new RequesterDataRequestComparator());

    private Comparator<Request> comparator;

    RequestComparator(Comparator<Request> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Request> getComparator() {
        return comparator;
    }
}
