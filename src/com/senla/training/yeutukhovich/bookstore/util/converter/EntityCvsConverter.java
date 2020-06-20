package com.senla.training.yeutukhovich.bookstore.util.converter;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityCvsConverter {

    private static final String DELIMITER = ",";

    public static List<String> convertBooks(List<Book> books) {
        List<String> bookStrings = new ArrayList<>();
        for (Book book : books) {
            String bookString = book.getId() +
                    DELIMITER +
                    book.getTitle() +
                    DELIMITER +
                    book.isAvailable() +
                    DELIMITER +
                    book.getEditionDate() +
                    DELIMITER +
                    book.getReplenishmentDate() +
                    DELIMITER +
                    book.getPrice();
            bookStrings.add(bookString);
        }
        return bookStrings;
    }

    public static List<Book> parseBooks(List<String> bookStrings) {
        List<Book> books = new ArrayList<>();
        for (String string : bookStrings) {
            String[] stringObjects = string.split(DELIMITER);
            if (stringObjects.length == 6) {
                try {
                    Book book = new Book(Long.valueOf(stringObjects[0]));
                    book.setTitle(stringObjects[1]);
                    if ("true".equals(stringObjects[2]) || "false".equals(stringObjects[2])) {
                        book.setAvailable(Boolean.valueOf((stringObjects[2])));
                    } else {
                        throw new IllegalArgumentException("Unparseable boolean: \"" + stringObjects[2] + "\"");
                    }
                    Date editionDate = DateConverter.parseDate(stringObjects[3], DateConverter.STANDARD_DATE_FORMAT);
                    if (editionDate == null) {
                        throw new IllegalArgumentException("Edition date can't be null");
                    } else {
                        book.setEditionDate(editionDate);
                    }
                    if (!"null".equals(stringObjects[4])) {
                        book.setReplenishmentDate(DateConverter.parseDate(stringObjects[4]
                                , DateConverter.STANDARD_DATE_FORMAT));
                    }
                    book.setPrice(BigDecimal.valueOf(Double.parseDouble(stringObjects[5])));
                    books.add(book);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return books;
    }
}
