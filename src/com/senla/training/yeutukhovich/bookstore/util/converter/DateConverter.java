package com.senla.training.yeutukhovich.bookstore.util.converter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateConverter {

    public static final SimpleDateFormat YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);


    public static Date parseDate(String dateString, SimpleDateFormat dateFormat) {
        Date date = null;
        if (dateString != null && dateFormat != null) {
            try {
                date = dateFormat.parse(dateString);
            } catch (java.text.ParseException e) {
                System.err.println(e.getMessage());
            }
        }
        return date;
    }

    public static String formatDate(Date date, SimpleDateFormat dateFormat) {
        if (date != null && dateFormat != null) {
            return dateFormat.format(date);
        }
        return "";
    }
}
