package com.senla.training.yeutukhovich.bookstore.util.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static final SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date parseDate(String dateString, SimpleDateFormat dateFormat) {

        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            System.err.println(e.getMessage());
        }
        return date;
    }

    public static String formatDate(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }
}
