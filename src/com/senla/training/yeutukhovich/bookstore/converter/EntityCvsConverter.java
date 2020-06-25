package com.senla.training.yeutukhovich.bookstore.converter;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IBookRepository;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityCvsConverter {

    private static EntityCvsConverter instance;
    private static final String DELIMITER = ";";

    private IBookRepository bookRepository = BookRepository.getInstance();

    private EntityCvsConverter() {

    }

    public static EntityCvsConverter getInstance() {
        if (instance == null) {
            instance = new EntityCvsConverter();
        }
        return instance;
    }


    public List<String> convertBooks(List<Book> books) {
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

    public List<Book> parseBooks(List<String> bookStrings) {
        List<Book> books = new ArrayList<>();
        for (String string : bookStrings) {
            String[] stringObjects = string.split(DELIMITER);
            if (stringObjects.length == 6) {
                try {
                    Book book = new Book();
                    book.setId(Long.valueOf(stringObjects[0]));
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

    public List<String> convertOrders(List<Order> orders) {
        List<String> orderStrings = new ArrayList<>();
        for (Order order : orders) {
            String orderString = order.getId() +
                    DELIMITER +
                    order.getBook().getId() +
                    DELIMITER +
                    order.getState() +
                    DELIMITER +
                    order.getCurrentBookPrice() +
                    DELIMITER +
                    order.getCreationDate() +
                    DELIMITER +
                    order.getCompletionDate() +
                    DELIMITER +
                    order.getCustomerData();
            orderStrings.add(orderString);
        }
        return orderStrings;
    }

    public List<Order> parseOrders(List<String> orderStrings) {
        List<Order> orders = new ArrayList<>();
        for (String string : orderStrings) {
            String[] stringObjects = string.split(DELIMITER);
            if (stringObjects.length == 7) {
                try {
                    Order order = new Order();
                    order.setId(Long.valueOf(stringObjects[0]));
                    Book book = bookRepository.findById(Long.valueOf(stringObjects[1]));
                    if (book == null) {
                        throw new IllegalArgumentException("Book can't be null.");
                    }
                    order.setBook(book);
                    order.setState(OrderState.valueOf(stringObjects[2]));
                    order.setCurrentBookPrice(BigDecimal.valueOf(Double.parseDouble(stringObjects[3])));
                    Date creationDate = DateConverter.parseDate(stringObjects[4], DateConverter.STANDARD_DATE_FORMAT);
                    if (creationDate == null) {
                        throw new IllegalArgumentException("Creation date can't be null");
                    } else {
                        order.setCreationDate(creationDate);
                    }
                    if (!"null".equals(stringObjects[5])) {
                        order.setCompletionDate(DateConverter.parseDate(stringObjects[5]
                                , DateConverter.STANDARD_DATE_FORMAT));
                    }
                    order.setCustomerData(stringObjects[6]);
                    orders.add(order);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return orders;
    }

    public List<String> convertRequests(List<Request> requests) {
        List<String> requestStrings = new ArrayList<>();
        for (Request request : requests) {
            String requestString = request.getId() +
                    DELIMITER +
                    request.getBook().getId() +
                    DELIMITER +
                    request.isActive() +
                    DELIMITER +
                    request.getRequesterData();
            requestStrings.add(requestString);
        }
        return requestStrings;
    }

    public List<Request> parseRequests(List<String> requestStrings) {
        List<Request> requests = new ArrayList<>();

        for (String requestString : requestStrings) {
            String[] stringObjects = requestString.split(DELIMITER);
            if (stringObjects.length == 4) {
                try {
                    Request request = new Request();
                    request.setId(Long.valueOf(stringObjects[0]));
                    Book book = bookRepository.findById(Long.valueOf(stringObjects[1]));
                    if (book == null) {
                        throw new IllegalArgumentException("Book can't be null.");
                    }
                    request.setBook(book);
                    if ("true".equals(stringObjects[2]) || "false".equals(stringObjects[2])) {
                        request.setActive(Boolean.valueOf((stringObjects[2])));
                    } else {
                        throw new IllegalArgumentException("Unparseable boolean: \"" + stringObjects[2] + "\"");
                    }
                    request.setRequesterData(stringObjects[3]);
                    requests.add(request);
                } catch (IllegalArgumentException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        return requests;
    }
}
