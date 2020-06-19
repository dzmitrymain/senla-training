package com.senla.training.yeutukhovich.bookstore.util.comparator.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.util.Comparator;

public class ReplenishmentDateBookComparator implements Comparator<Book> {

    @Override
    public int compare(Book o1, Book o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            if (o1.getReplenishmentDate() == null && o2.getReplenishmentDate() == null) {
                return 0;
            } else if (o1.getReplenishmentDate() == null) {
                return 1;
            } else if (o2.getReplenishmentDate() == null) {
                return -1;
            } else {
                return o1.getReplenishmentDate().compareTo(o2.getReplenishmentDate());
            }
        }
    }
}
