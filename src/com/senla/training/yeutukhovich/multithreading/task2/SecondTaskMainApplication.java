package com.senla.training.yeutukhovich.multithreading.task2;

public class SecondTaskMainApplication {

    public static void main(String[] args) {
        new Thread(new PrintNameTask(), "FirstThread").start();
        new Thread(new PrintNameTask(), "SecondThread").start();
    }

    private static synchronized void printThreadName() throws InterruptedException {
        System.out.println(Thread.currentThread().getName());

        SecondTaskMainApplication.class.notify();
        SecondTaskMainApplication.class.wait();
    }

    private static class PrintNameTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    printThreadName();
                } catch (InterruptedException ignored) {
                    //ignored
                }
            }
        }
    }
}
