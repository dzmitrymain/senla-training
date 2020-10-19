package com.senla.training.yeutukhovich.bookstore.converter;

import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class EntityCsvConverter {

    private static final String DELIMITER = ";";

    public List<String> convertBooks(List<Book> books) {
        List<String> bookStrings = new ArrayList<>();
        for (Book book : books) {
            String bookString = book.getId() +
                    DELIMITER +
                    book.getTitle() +
                    DELIMITER +
                    book.getAvailable() +
                    DELIMITER +
                    book.getEditionYear() +
                    DELIMITER +
                    new Date(book.getReplenishmentDate().getTime()) +
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
            if (book == null) {
                continue;
            }
            books.add(book);
        }
        return books;
    }

    public List<String> convertOrders(List<Order> orders) {
        List<String> orderStrings = new ArrayList<>();
        for (Order order : orders) {
            String completionDateString = order.getCompletionDate() == null ?
                    null : new Date(order.getCompletionDate().getTime()).toString();
            String orderString = order.getId() +
                    DELIMITER +
                    order.getBook().getId() +
                    DELIMITER +
                    order.getState() +
                    DELIMITER +
                    order.getCurrentBookPrice() +
                    DELIMITER +
                    new Date(order.getCreationDate().getTime()) +
                    DELIMITER +
                    completionDateString +
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
            if (order == null) {
                continue;
            }
            orders.add(order);
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
                    request.getActive() +
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
            if (request == null) {
                continue;
            }
            requests.add(request);
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
        throw new IllegalArgumentException(String.format(MessageConstant.STRING_FORMAT_UNPARSEABLE_BOOLEAN.getMessage(),
                string));
    }

    private Book buildBook(String[] strings) {
        if (strings.length == 6) {
            try {
                Book book = new Book();
                book.setId(Long.valueOf(strings[0]));
                book.setTitle(strings[1]);
                book.setAvailable(parseBoolean(strings[2]));
                book.setEditionYear(Integer.parseInt(strings[3]));
                if (!"null".equals(strings[4])) {
                    book.setReplenishmentDate(DateConverter.parseDate(strings[4],
                            DateConverter.STANDARD_DATE_FORMAT));
                }
                book.setPrice(BigDecimal.valueOf(Double.parseDouble(strings[5])));
                return book;
            } catch (IllegalArgumentException | ParseException e) {
                throw new BusinessException(MessageConstant.CANT_PARSE_BOOK.getMessage() + " " + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    private Order buildOrder(String[] strings) {
        if (strings.length == 7) {
            try {
                Order order = new Order();
                order.setId(Long.valueOf(strings[0]));
                order.setBook(new Book(Long.valueOf(strings[1])));
                new Book();
                order.setState(OrderState.valueOf(strings[2]));
                order.setCurrentBookPrice(BigDecimal.valueOf(Double.parseDouble(strings[3])));
                Date creationDate = DateConverter.parseDate(strings[4], DateConverter.STANDARD_DATE_FORMAT);
                if (creationDate == null) {
                    throw new IllegalArgumentException(MessageConstant.CREATION_NOT_NULL.getMessage());
                } else {
                    order.setCreationDate(creationDate);
                }
                if (!"null".equals(strings[5])) {
                    order.setCompletionDate(DateConverter.parseDate(strings[5],
                            DateConverter.STANDARD_DATE_FORMAT));
                }
                order.setCustomerData(strings[6]);
                return order;
            } catch (IllegalArgumentException | ParseException e) {
                throw new BusinessException(MessageConstant.CANT_PARSE_ORDER.getMessage() + " " + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }

    private Request buildRequest(String[] strings) {
        if (strings.length == 4) {
            try {
                Request request = new Request();
                request.setId(Long.valueOf(strings[0]));
                request.setBook(new Book(Long.valueOf(strings[1])));
                request.setActive(parseBoolean(strings[2]));
                request.setRequesterData(strings[3]);
                return request;
            } catch (IllegalArgumentException e) {
                throw new BusinessException(MessageConstant.CANT_PARSE_REQUEST.getMessage() + " " + e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }
}
