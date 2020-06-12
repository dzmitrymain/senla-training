package com.senla.training.yeutukhovich.bookstore.util.converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    public static final String YEAR_DATE_FORMAT = "yyyy";
    public static final String DAY_DATE_FORMAT = "yyyy-MM-dd";


    public static Date parseDate(String dateString, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateString);
        } catch (java.text.ParseException e) {
            System.err.println(e.getMessage());
        }
        return date;
    }

    public static String formatDate(Date date, String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(date);
    }
}
