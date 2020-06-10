package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.book.TitleBookComparator;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.order.PriceOrderComparator;
import com.senla.training.yeutukhovich.bookstore.service.BookstoreService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookstoreServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;

public class MainApp {

    public static void main(String[] args) {

        BookstoreService bookstoreService = new BookstoreServiceImpl(initBooks(), new Order[1], new Request[1]);

        Book[] books = bookstoreService.findAllBooks(new TitleBookComparator());

        for (int i = 0, j = 0; i < books.length; i++) {
            bookstoreService.createOrder(books[i], "Customer" + ++j);
        }

        for (Book book : books) {
            if (!book.isAvailable()) {
                bookstoreService.replenishBook(book);
            }
        }

        Order[] orders = bookstoreService.findAllOrders(new PriceOrderComparator());
        for (Order order : orders) {
            bookstoreService.completeOrder(order);
        }
    }

    private static Book[] initBooks() {
        return new Book[]{
                new Book("Jonathan Livingston Seagull", true, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), BigDecimal.valueOf(25.90)),
                new Book("Hard to be a god", true, DateConverter.parseDate("1964", DateConverter.YEAR_DATE_FORMAT), BigDecimal.valueOf(29.99)),
                new Book("Hotel \"At a Lost Climber\"", false, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), BigDecimal.valueOf(22.50)),
                new Book("Roadside Picnic", false, DateConverter.parseDate("1972", DateConverter.YEAR_DATE_FORMAT), BigDecimal.valueOf(19.95)),
                new Book("Prisoners of Power", true, DateConverter.parseDate("1971", DateConverter.YEAR_DATE_FORMAT), BigDecimal.valueOf(35.90))};
    }
}
