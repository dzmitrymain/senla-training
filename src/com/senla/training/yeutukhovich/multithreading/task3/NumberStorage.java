package com.senla.training.yeutukhovich.multithreading.task3;

import java.util.ArrayList;
import java.util.List;

public class NumberStorage {
    private int storageSize;
    private List<Integer> numbers;

    protected NumberStorage(int storageSize) {
        this.storageSize = storageSize;
        numbers = new ArrayList<>(storageSize);
    }

    public synchronized void put(Integer number) throws InterruptedException {
        while (numbers.size() >= storageSize) {
            wait();
        }
        numbers.add(number);
        System.out.println(Thread.currentThread().getName() + " put the number in storage.");
        System.out.println("Numbers at storage: " + numbers.size());
        notify();
    }

    public synchronized Integer get() throws InterruptedException {
        while (numbers.isEmpty()) {
            wait();
        }
        Integer number = numbers.remove(0);
        System.out.println(Thread.currentThread().getName() + " get the number from storage.");
        System.out.println("Numbers at storage: " + numbers.size());
        notify();
        return number;
    }
}
