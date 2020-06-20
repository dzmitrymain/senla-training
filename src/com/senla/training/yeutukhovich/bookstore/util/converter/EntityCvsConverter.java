package com.senla.training.yeutukhovich.bookstore.util.converter;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.math.BigDecimal;
import java.util.ArrayList;
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
                    book.setAvailable(Boolean.valueOf(stringObjects[2]));
                    book.setEditionDate(DateConverter.parseDate(stringObjects[3], DateConverter.STANDARD_DATE_FORMAT));
                    if (!stringObjects[4].equals("null")) {
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
