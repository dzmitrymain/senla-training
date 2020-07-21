package com.senla.training.yeutukhovich.bookstore.ui.util.reader;

import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserInputReader {

    private static final BufferedReader READER = new BufferedReader(new InputStreamReader(System.in));

    private UserInputReader() {

    }

    public static String readInputString() {
        try {
            return READER.readLine();
        } catch (IOException e) {
            UiConsolePrinter.printError(e.getMessage());
        }
        return null;
    }

    public static Date readInputDate(SimpleDateFormat simpleDateFormat) {
        if (simpleDateFormat != null) {
            try {
                return DateConverter.parseDate(READER.readLine(), simpleDateFormat);
            } catch (IOException | ParseException e) {
                UiConsolePrinter.printError(e.getMessage());
            }
        }
        return null;
    }

    public static Long readInputLong() {
        try {
            return Long.valueOf(READER.readLine());
        } catch (NumberFormatException | IOException e) {
            UiConsolePrinter.printError(e.getMessage());
        }
        return null;
    }

    public static Integer readInputInteger() {
        try {
            return Integer.valueOf(READER.readLine());
        } catch (NumberFormatException | IOException e) {
            UiConsolePrinter.printError(e.getMessage());
        }
        return null;
    }

    public static void closeReader() {
        try {
            READER.close();
        } catch (IOException e) {
            //log
        }
    }
}
