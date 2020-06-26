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
            Book book = buildBook(stringObjects);
            if (book != null) {
                books.add(book);
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
            Order order = buildOrder(stringObjects);
            if (order != null) {
                orders.add(order);
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
            Request request = buildRequest(stringObjects);
            if (request != null) {
                requests.add(request);
            }
        }
        return requests;
    }

    private boolean parseBoolean(String string) {
        if ("true".equals(string)) {
            return true;
        }
        if ("false".equals(string)) {
            return false;
        }
        throw new IllegalArgumentException("Unparseable boolean: \"" + string + "\"");
    }

    private Book buildBook(String[] strings) {
        Book book = null;
        if (strings.length == 6) {
            try {
                book = new Book();
                book.setId(Long.valueOf(strings[0]));
                book.setTitle(strings[1]);
                book.setAvailable(parseBoolean(strings[2]));
                Date editionDate = DateConverter.parseDate(strings[3], DateConverter.STANDARD_DATE_FORMAT);
                if (editionDate == null) {
                    throw new IllegalArgumentException("Edition date can't be null");
                } else {
                    book.setEditionDate(editionDate);
                }
                if (!"null".equals(strings[4])) {
                    book.setReplenishmentDate(DateConverter.parseDate(strings[4]
                            , DateConverter.STANDARD_DATE_FORMAT));
                }
                book.setPrice(BigDecimal.valueOf(Double.parseDouble(strings[5])));
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        return book;
    }

    private Order buildOrder(String[] strings) {
        Order order = null;
        if (strings.length == 7) {
            order = new Order();
            try {
                order = new Order();
                order.setId(Long.valueOf(strings[0]));
                Book book = bookRepository.findById(Long.valueOf(strings[1]));
                if (book == null) {
                    throw new IllegalArgumentException("Book can't be null.");
                }
                order.setBook(book);
                order.setState(OrderState.valueOf(strings[2]));
                order.setCurrentBookPrice(BigDecimal.valueOf(Double.parseDouble(strings[3])));
                Date creationDate = DateConverter.parseDate(strings[4], DateConverter.STANDARD_DATE_FORMAT);
                if (creationDate == null) {
                    throw new IllegalArgumentException("Creation date can't be null");
                } else {
                    order.setCreationDate(creationDate);
                }
                if (!"null".equals(strings[5])) {
                    order.setCompletionDate(DateConverter.parseDate(strings[5]
                            , DateConverter.STANDARD_DATE_FORMAT));
                }
                order.setCustomerData(strings[6]);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        return order;
    }

    private Request buildRequest(String[] strings) {
        Request request = null;
        if (strings.length == 4) {
            try {
                request = new Request();
                request.setId(Long.valueOf(strings[0]));
                Book book = bookRepository.findById(Long.valueOf(strings[1]));
                if (book == null) {
                    throw new IllegalArgumentException("Book can't be null.");
                }
                request.setBook(book);
                request.setActive(parseBoolean(strings[2]));
                request.setRequesterData(strings[3]);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
        return request;
    }
}
