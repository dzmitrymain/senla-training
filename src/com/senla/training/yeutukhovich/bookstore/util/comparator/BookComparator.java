package com.senla.training.yeutukhovich.bookstore.util.comparator;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.*;

import java.util.Comparator;

public enum BookComparator {

    ID(new IdBookComparator()),
    AVAILABILITY(new AvailabilityBookComparator()),
    EDITION_DATE(new EditionDateBookComparator()),
    PRICE(new PriceBookComparator()),
    REPLENISHMENT_DATE(new ReplenishmentDateBookComparator()),
    TITLE(new TitleBookComparator());

    private Comparator<Book> comparator;

    BookComparator(Comparator<Book> comparator) {
        this.comparator = comparator;
    }

    public Comparator<Book> getComparator() {
        return comparator;
    }
}
