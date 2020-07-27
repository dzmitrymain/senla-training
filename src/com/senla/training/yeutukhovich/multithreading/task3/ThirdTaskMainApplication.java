package com.senla.training.yeutukhovich.multithreading.task3;

public class ThirdTaskMainApplication {

    public static void main(String[] args) {
        NumberStorage storage = new NumberStorage(5);

        Producer producer = new Producer(storage);
        Consumer consumer = new Consumer(storage);

        new Thread(producer, "Producer").start();
        new Thread(consumer, "Consumer").start();
    }
}

