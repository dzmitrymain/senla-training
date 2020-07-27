package com.senla.training.yeutukhovich.multithreading.task4;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTimeDaemonThread extends Thread {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private int millisDelay;

    SystemTimeDaemonThread(int secondsDelay) {
        millisDelay = secondsDelay * 1000;
        this.setDaemon(true);
        start();
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                System.out.println(formatTime(System.currentTimeMillis()));
                Thread.sleep(millisDelay);
            } catch (InterruptedException ignored) {
                //ignored
            }
        }
    }

    private static String formatTime(long time) {
        return TIME_FORMAT.format(new Date(time));
    }
}
