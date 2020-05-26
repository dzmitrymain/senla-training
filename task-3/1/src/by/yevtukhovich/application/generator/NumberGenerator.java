package by.yevtukhovich.application.generator;

import java.util.Random;

public class NumberGenerator {

    public static int generateNumber(int minValue, int maxValue) {
        return new Random().nextInt((maxValue - minValue) + 1) + minValue;
    }
}
