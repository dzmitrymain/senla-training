package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.impl.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.service.impl.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.TitleBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.PriceOrderComparator;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.Date;

public class MainApp {

    public static void main(String[] args) {

//        EntityRepository<Book> bookRepository = new EntityRepository<>(initBooks());
//        EntityRepository<Order> orderRepository = new EntityRepository<>(new Order[1]);
//        EntityRepository<Request> requestRepository = new EntityRepository<>(new Request[1]);

       // BookService bookService = new BookServiceImpl(bookRepository, orderRepository, requestRepository);
       // OrderService orderService = new OrderServiceImpl(bookRepository, orderRepository, requestRepository);

//        Book[] books = bookService.findAllBooks(new TitleBookComparator());
//
//        for (int i = 0, j = 0; i < books.length; i++) {
//            orderService.createOrder(books[i], "Customer" + ++j);
//        }
//
//        for (Book book : books) {
//            if (!book.isAvailable()) {
//                bookService.replenishBook(book.getId());
//            }
//        }
//
//        Order[] orders = orderService.findAllOrders(new PriceOrderComparator());
//        for (Order order : orders) {
//            orderService.completeOrder(order);
//        }
    }

    private static Book[] initBooks() {
        return new Book[]{
                new Book("Jonathan Livingston Seagull", true, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(25.90)),
                new Book("Hard to be a god", true, DateConverter.parseDate("1964", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(29.99)),
                new Book("Hotel \"At a Lost Climber\"", false, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT), null, BigDecimal.valueOf(22.50)),
                new Book("Roadside Picnic", false, DateConverter.parseDate("1972", DateConverter.YEAR_DATE_FORMAT), null, BigDecimal.valueOf(19.95)),
                new Book("Prisoners of Power", true, DateConverter.parseDate("1971", DateConverter.YEAR_DATE_FORMAT), new Date(), BigDecimal.valueOf(35.90))};
    }
}
