package com.senla.training.yeutukhovich.bookstore.util.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class DateConverter {

    public static final SimpleDateFormat DAY_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat STANDARD_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

    private DateConverter() {

    }

    public static Date parseDate(String dateString, SimpleDateFormat dateFormat) throws ParseException {
        if (dateString != null && dateFormat != null) {
            return dateFormat.parse(dateString);
        }
        return null;
    }

    public static String formatDate(Date date, SimpleDateFormat dateFormat) {
        if (date != null && dateFormat != null) {
            return dateFormat.format(date);
        }
        return "";
    }
}
