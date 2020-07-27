package com.senla.training.yeutukhovich.multithreading.task1;

public class FirstTaskMainApplication {

    private static final Thread FIRST_THREAD = new FirstThread();
    private static final Thread SECOND_THREAD = new SecondThread();

    public static void main(String[] args) throws InterruptedException {
        System.out.println(FIRST_THREAD.getState()); //NEW

        FIRST_THREAD.start();
        SECOND_THREAD.start();

        Thread.sleep(50);
        System.out.println(FIRST_THREAD.getState()); //TIMED_WAITING
        Thread.sleep(100);
        System.out.println(FIRST_THREAD.getState()); //BLOCKED
        Thread.sleep(300);
        System.out.println(FIRST_THREAD.getState()); //WAITING

        FIRST_THREAD.join();
        System.out.println(FIRST_THREAD.getState()); //TERMINATED
    }

    private static synchronized void blockingMethod() throws InterruptedException {
        Thread.sleep(200);
    }

    private static class FirstThread extends Thread {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getState()); //RUNNABLE
            try {
                Thread.sleep(100);
                blockingMethod();
                SECOND_THREAD.join();
            } catch (InterruptedException ignored) {
                //ignored
            }
        }
    }

    private static class SecondThread extends Thread {
        @Override
        public void run() {
            try {
                blockingMethod();
                Thread.sleep(300);
            } catch (InterruptedException ignored) {
                //ignored
            }
        }
    }
}
