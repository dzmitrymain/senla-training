package com.senla.training.yeutukhovich.bookstore.serializer;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.*;
import com.senla.training.yeutukhovich.bookstore.util.constant.PathConstant;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;

import java.io.*;
import java.util.List;

public class BookstoreSerializer {

    private static BookstoreSerializer instance;

    private IBookRepository bookRepository = BookRepository.getInstance();
    private IOrderRepository orderRepository = OrderRepository.getInstance();
    private IRequestRepository requestRepository = RequestRepository.getInstance();

    private BookstoreSerializer() {

    }

    public static BookstoreSerializer getInstance() {
        if (instance == null) {
            instance = new BookstoreSerializer();
        }
        return instance;
    }

    public void serializeBookstore() {

        SerializedDataObject serializedDataObject = new SerializedDataObject();
        serializedDataObject.setBooks(bookRepository.findAll());
        serializedDataObject.setOrders(orderRepository.findAll());
        serializedDataObject.setRequests(requestRepository.findAll());

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(PathConstant.SERIALIZED_DATA_PATH.getPathConstant()))) {
            out.writeObject(serializedDataObject);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void deserializeBookstore() {
        SerializedDataObject serializedDataObject = null;

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(PathConstant.SERIALIZED_DATA_PATH.getPathConstant()))) {
            serializedDataObject = (SerializedDataObject) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }

        if (serializedDataObject != null) {
            refillRepositories(serializedDataObject);
        }
    }

    private void refillRepositories(SerializedDataObject serializedDataObject) {
        if (serializedDataObject.getBooks() != null && !serializedDataObject.getBooks().isEmpty()) {
            refillBookRepository(serializedDataObject.getBooks());
        } else {
            refillBookRepository(EntityInitializer.initBooks());
        }
        if (serializedDataObject.getOrders() != null && !serializedDataObject.getOrders().isEmpty()) {
            refillOrderRepository(serializedDataObject.getOrders());
        }
        if (serializedDataObject.getRequests() != null && !serializedDataObject.getRequests().isEmpty()) {
            refillRequestRepository(serializedDataObject.getRequests());
        }
    }

    private void refillBookRepository(List<Book> books) {
        for (Book book : books) {
            bookRepository.add(book);
        }
    }

    private void refillOrderRepository(List<Order> orders) {
        for (Order order : orders) {
            orderRepository.add(order);
        }
    }

    private void refillRequestRepository(List<Request> requests) {
        for (Request request : requests) {
            requestRepository.add(request);
        }
    }

    private static class SerializedDataObject implements Serializable {

        private static final long serialVersionUID = -5017858520252952975L;

        private List<Book> books;
        private List<Order> orders;
        private List<Request> requests;

        List<Book> getBooks() {
            return books;
        }

        void setBooks(List<Book> books) {
            this.books = books;
        }

        List<Order> getOrders() {
            return orders;
        }

        void setOrders(List<Order> orders) {
            this.orders = orders;
        }

        List<Request> getRequests() {
            return requests;
        }

        void setRequests(List<Request> requests) {
            this.requests = requests;
        }
    }
}

