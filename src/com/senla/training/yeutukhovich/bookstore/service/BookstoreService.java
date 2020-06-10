package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

public interface BookstoreService {

    void replenishBook(Book book);

    void writeOffBook(Book book);

    Order createOrder(Book book, String customerData);

    void cancelOrder(Order order);

    void completeOrder(Order order);

    Request createRequest(Book book, String requesterData);

    Book[] findAllBooks(Comparator<Book> bookComparator);

    Order[] findAllOrders(Comparator<Order> orderComparator);

    Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate, Comparator<Order> orderComparator);

    Request[] findBookRequests(Book book, Comparator<Request> requestComparator);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    Book[] findSoldBooksBetweenDates(Date startDate, Date endDate);

    Book[] findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    Book[] findStaleBooks();

    OrderDetails showOrderDetails(Order order);

    BookDescription showBookDescription(Book book);
}
