package com.senla.training.yeutukhovich.multithreading.task3;

public class Producer implements Runnable {
    private NumberStorage storage;

    // модификатор доступа?
    Producer(NumberStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                // Random.nextInt()
                storage.put((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted.");
            }
        }
    }
}
