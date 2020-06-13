package com.senla.training.yeutukhovich.bookstore.controller.menu.util.reader;

import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputReader {

    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static Date readInputDate(SimpleDateFormat simpleDateFormat) {
        Date input = null;
        try {
            input = DateConverter.parseDate(bufferedReader.readLine(), simpleDateFormat);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return input;
    }

    public static Long readInputLong() {
        Long input = null;
        try {
            input = Long.valueOf(bufferedReader.readLine());
        } catch (NumberFormatException | IOException e) {
            System.err.println(e.getMessage());
        }
        return input;
    }

    public static Integer readInputInteger() {
        Integer input = null;
        try {
            input = Integer.valueOf(bufferedReader.readLine());
        } catch (NumberFormatException | IOException e) {
            System.err.println(e.getMessage());
        }
        return input;
    }
}
