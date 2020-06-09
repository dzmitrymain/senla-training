package com.senla.training.yeutukhovich.bookstore.util.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static final String YEAR_DATE_FORMAT = "yyyy";
    // не используется
    public static final String DAY_DATE_FORMAT = "yyyy-MM-dd";

    public static Date parseDate(String dateString, String dateFormat) {
        // при вызове этого утилитного метода каждый раз будет создаваться объект SimpleDateFormat
        // но создание объектов - это дорогая операция, в данном случае надо было вынести в поле класса
        // (приватное, финальное, статическое) и использовать его
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            // потихоньку начинаем забывать про это, через одну домашку начну сильно ругать за это
            // попробуй пока что что-то типа
            //System.out.println("Error message");
            // или
            //System.err.println("Error message");
            e.printStackTrace();
        }
        return date;
    }

    public static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }
}
