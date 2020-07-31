package com.senla.training.yeutukhovich.multithreading.task4;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemTimeDaemonThread extends Thread {

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private int millisDelay;

    // модификатор доступа?
    SystemTimeDaemonThread(int secondsDelay) {
        millisDelay = secondsDelay * 1000;
        // хотел сказать, что не совсем правильно делать это в конструкторе,
        // но твой класс называется SystemTimeDaemonThread - что подчеркивает, что это демон
        // Но если бы такой подсказки не было, то другой разработчик мог создать поток из этого
        // класса и неожиданно для себя получить демона вместо потока
        // в общем, все ок, ошибки нет, но имей в виду
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
        // конструктор по умолчанию new Date() вернет текущее время, вызвав
        // самостоятельно System.currentTimeMillis() - посмотри реализацию этого конструктора
        return TIME_FORMAT.format(new Date(time));
    }
}
