package com.senla.training.yeutukhovich.bookstore.util.comparator.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.util.Comparator;

// можно было не делать синглтоном
// для хранения компараторов круто подходит enum (для каждого типа модели свой енам)
// а вообще можно было попробовать лямбды, ссылку на статью я кидал в чат, там как раз на примере
// компараторов разбирали
public class AvailabilityBookComparator implements Comparator<Book> {

    private static AvailabilityBookComparator instance;

    public static AvailabilityBookComparator getInstance() {
        if (instance == null) {
            instance = new AvailabilityBookComparator();
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
            // как вариант
            // return o2.isAvailable().compareTo(o1.isAvailable())
            return Boolean.compare(o2.isAvailable(), o1.isAvailable());
        }
    }
}
