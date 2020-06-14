package com.senla.training.yeutukhovich.bookstore.util.comparator.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.util.Comparator;

public class TitleBookComparator implements Comparator<Book> {

    private static TitleBookComparator instance;

    public static TitleBookComparator getInstance() {
        if (instance == null) {
            instance = new TitleBookComparator();
        }
        return instance;
    }

    @Override
    public int compare(Book o1, Book o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return o1.getTitle().compareTo(o2.getTitle());
        }
    }
}