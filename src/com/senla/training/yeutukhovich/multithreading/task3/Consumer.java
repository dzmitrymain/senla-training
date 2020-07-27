package com.senla.training.yeutukhovich.multithreading.task3;

public class Consumer implements Runnable {

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
