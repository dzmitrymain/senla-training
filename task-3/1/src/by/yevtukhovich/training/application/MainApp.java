package by.yevtukhovich.training.application;
import by.yevtukhovich.training.application.generator.NumberGenerator;
import by.yevtukhovich.training.application.util.MathOperations;

public class MainApp {

    private static final int MIN_VALUE =100;
    private static final int MAX_VALUE =999;

    public static void main(String[] args) {

        int randomNumber= NumberGenerator.generateNumber(MIN_VALUE, MAX_VALUE);
        int maxDigit= MathOperations.findMaxDigitInNumber(randomNumber);

        System.out.println("Random number: " + randomNumber);
        System.out.println("Max digit: " + maxDigit);
    }
}
