package com.senla.training.yeutukhovich.multithreading.task2;

public class SecondTaskMainApplication {

    public static void main(String[] args) {
        Runnable printNameTask = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    printThreadName();
                } catch (InterruptedException ignored) {
                    //ignored
                }
            }
        };
        new Thread(printNameTask, "FirstThread").start();
        new Thread(printNameTask, "SecondThread").start();
    }

    private static synchronized void printThreadName() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        SecondTaskMainApplication.class.notify();
        SecondTaskMainApplication.class.wait();

    }
}
