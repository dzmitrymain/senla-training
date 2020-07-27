package com.senla.training.yeutukhovich.multithreading.task3;

import java.util.ArrayList;
import java.util.List;

public class ThirdTaskMainApplication {

    public static void main(String[] args) {
        NumberStorage storage = new NumberStorage(5);

        Producer producer = new Producer(storage);
        Consumer consumer = new Consumer(storage);

        new Thread(producer, "Producer").start();
        new Thread(consumer, "Consumer").start();
    }
}

class Producer implements Runnable {

    private NumberStorage storage;

    Producer(NumberStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                storage.put((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted.");
            }
        }
    }
}

class Consumer implements Runnable {

    private NumberStorage storage;

    Consumer(NumberStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                storage.get();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted.");
            }
        }
    }
}

class NumberStorage {
    private int storageSize;
    private List<Integer> numbers;

    NumberStorage(int storageSize) {
        this.storageSize = storageSize;
        numbers = new ArrayList<>(storageSize);
    }

    synchronized void put(Integer number) throws InterruptedException {
        while (numbers.size() >= storageSize) {
            wait();
        }
        numbers.add(number);
        System.out.println(Thread.currentThread().getName() + " put the number in storage.");
        System.out.println("Numbers at storage: " + numbers.size());
        notify();
    }

    synchronized Integer get() throws InterruptedException {
        while (numbers.size() == 0) {
            wait();
        }
        Integer number = numbers.remove(0);
        System.out.println(Thread.currentThread().getName() + " get the number from storage.");
        System.out.println("Numbers at storage: " + numbers.size());
        notify();
        return number;
    }
}
