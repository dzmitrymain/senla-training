package com.senla.training.yeutukhovich.bookstore.util.generator;

public class IdGenerator {

    private static IdGenerator instance;

    private long bookIdNumber;
    private long orderIdNumber;
    private long requestIdNumber;

    public static IdGenerator getInstance() {
        if (instance == null) {
            instance = new IdGenerator();
        }
        return instance;
    }

    public long getNextBookIdNumber(){
        return ++bookIdNumber;
    }

    public long getNextOrderIdNumber(){
        return ++orderIdNumber;
    }

    public long getNextRequestIdNumber(){
        return ++requestIdNumber;
    }
}
