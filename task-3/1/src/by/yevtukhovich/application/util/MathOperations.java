package by.yevtukhovich.application.util;

public class MathOperations {

    public static int findMaxDigitInNumber(int number){

        char[] numberChars = Integer.toString(number).toCharArray();
        int maxDigit = 0;
        for (char numberChar : numberChars) {
            int numericValue = Character.getNumericValue(numberChar);
            if (numericValue > maxDigit) {
                maxDigit = numericValue;
            }
        }
        return maxDigit;
    }
}
