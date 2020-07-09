package com.senla.training.yeutukhovich.bookstore.serializer;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.io.Serializable;
import java.util.List;

public class ApplicationState implements Serializable {

    private static final long serialVersionUID = -4013915498899203870L;

    private List<Book> books;
    private List<Order> orders;
    private List<Request> requests;

    public ApplicationState(){

    }

    public ApplicationState(List<Book> books, List<Order> orders, List<Request> requests) {
        this.books = books;
        this.orders = orders;
        this.requests = requests;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
