// обычно компараторы не лежат в пакете с моделями, по функции это ближе к утилитам
package com.senla.training.yeutukhovich.bookstore.domain.comparator.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.util.Comparator;

// не испольуется
public class AvailabilityBookComparator implements Comparator<Book> {

    @Override
    public int compare(Book o1, Book o2) {
        if (o1 == null && o2 == null) {
            return 0;
        } else if (o1 == null) {
            return 1;
        } else if (o2 == null) {
            return -1;
        } else {
            return Boolean.compare(o2.isAvailable(), o1.isAvailable());
        }
    }
}
