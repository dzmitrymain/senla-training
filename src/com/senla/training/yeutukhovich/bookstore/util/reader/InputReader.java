package com.senla.training.yeutukhovich.bookstore.util.reader;

import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InputReader {

    // может быть final - тогда это константа (имя в верхнем регистре)
    private static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

    public static String readInputString() {
        // как вариант (чуть компактней, без лишних переменных и более линейно)
        /*
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return null;
        }
         */

        String string = null;
        try {
            string = bufferedReader.readLine();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return string;
    }

    public static Date readInputDate(SimpleDateFormat simpleDateFormat) {
        Date input = null;
        if (simpleDateFormat != null) {
            try {
                input = DateConverter.parseDate(bufferedReader.readLine(), simpleDateFormat);
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
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
