package com.senla.training.yeutukhovich.multithreading.task4;

public class FourthTaskMainApplication {

    public static void main(String[] args) throws InterruptedException {
        new SystemTimeDaemonThread(1);
        Thread.sleep(10000);
    }
}
