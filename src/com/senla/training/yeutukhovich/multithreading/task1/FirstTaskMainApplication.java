package com.senla.training.yeutukhovich.multithreading.task1;

import java.math.BigInteger;

public class FirstTaskMainApplication {

    private static final Thread MY_THREAD = new Thread(() -> {
        try {
            doWork();
            doSleep();
        } catch (InterruptedException ignored) {
        }
    });

    public static void main(String[] args) throws InterruptedException {
        System.out.println(MY_THREAD.getState()); //NEW
        MY_THREAD.start();
        Thread.sleep(150);
        System.out.println(MY_THREAD.getState()); //RUNNABLE
        Thread.sleep(250);
        System.out.println(MY_THREAD.getState()); //WAITING
        doSleep();
        Thread.sleep(50);
        System.out.println(MY_THREAD.getState()); //TIMED-WAITING
        MY_THREAD.join();
        System.out.println(MY_THREAD.getState()); //TERMINATED
    }

    private static synchronized void doWork() throws InterruptedException {  //250-350ms
        BigInteger count = new BigInteger("1");
        for (int i = 1; i < 20_000; i++) {
            count = count.multiply(new BigInteger(String.valueOf(i)));
        }
        FirstTaskMainApplication.class.wait();
    }

    private static synchronized void doSleep() throws InterruptedException {
        FirstTaskMainApplication.class.notify();
        Thread.sleep(200);
        if (!Thread.currentThread().equals(MY_THREAD)) {
            System.out.println(MY_THREAD.getState()); //BLOCKED
        }
    }
}
