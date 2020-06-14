package com.senla.training.yeutukhovich.bookstore.util.comparator.request;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.Comparator;

public class BookIdRequestComparator implements Comparator<Request> {

    private static BookIdRequestComparator instance;

    public static BookIdRequestComparator getInstance() {
        if (instance == null) {
            instance = new BookIdRequestComparator();
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
            if (o1.getBook() == null && o2.getBook() == null) {
                return 0;
            } else if (o1.getBook() == null) {
                return 1;
            } else if (o2.getBook() == null) {
                return -1;
            } else {
                if (o1.getBook().getTitle() == null && o2.getBook().getTitle() == null) {
                    return 0;
                } else if (o1.getBook().getTitle() == null) {
                    return 1;
                } else if (o2.getBook().getTitle() == null) {
                    return -1;
                } else {
                    return o2.getBook().getTitle().compareTo(o1.getBook().getTitle());
                }
            }
        }
    }
}
