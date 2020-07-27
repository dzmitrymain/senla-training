package com.senla.training.yeutukhovich.bookstore.util.generator;

import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

@Singleton
public class IdGenerator {

    private long bookIdNumber;
    private long orderIdNumber;
    private long requestIdNumber;

    private IdGenerator() {

    }

    public long getNextBookIdNumber() {
        return ++bookIdNumber;
    }

    public long getNextOrderIdNumber() {
        return ++orderIdNumber;
    }

    public long getNextRequestIdNumber() {
        return ++requestIdNumber;
    }
}
