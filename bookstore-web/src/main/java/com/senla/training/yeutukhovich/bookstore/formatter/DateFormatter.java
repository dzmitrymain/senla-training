package com.senla.training.yeutukhovich.bookstore.formatter;

import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class DateFormatter implements Formatter<Date> {

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        return DateConverter.parseDate(text, DateConverter.DAY_DATE_FORMAT);
    }

    @Override
    public String print(Date date, Locale locale) {
        return DateConverter.formatDate(date, DateConverter.DAY_DATE_FORMAT);
    }
}
