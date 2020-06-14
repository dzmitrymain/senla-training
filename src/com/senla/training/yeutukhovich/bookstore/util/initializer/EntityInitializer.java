package com.senla.training.yeutukhovich.bookstore.util.initializer;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

public class EntityInitializer {

    private static ArrayList<Book> books;
    private static ArrayList<Order> orders;
    private static ArrayList<Request> requests;

    public static ArrayList<Book> getBooks() {
        if (books == null) {
            books = new ArrayList<>();
            books.add(new Book("Jonathan Livingston Seagull", true, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(25.90)));
            books.add(new Book("Hard to be a god", true, DateConverter.parseDate("1964", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(29.99)));
            books.add(new Book("Hotel \"At a Lost Climber\"", false, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), null, BigDecimal.valueOf(22.50)));
            books.add(new Book("Roadside Picnic", false, DateConverter.parseDate("1972", DateConverter.YEAR_DATE_FORMAT), null, BigDecimal.valueOf(19.95)));
            books.add(new Book("Prisoners of Power", true, DateConverter.parseDate("1971", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(35.90)));
        }
        return books;
    }

    public static ArrayList<Order> getOrders() {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        return orders;
    }

    public static ArrayList<Request> getRequests() {
        if (requests == null) {
            requests = new ArrayList<>();
        }
        return requests;
    }
}

