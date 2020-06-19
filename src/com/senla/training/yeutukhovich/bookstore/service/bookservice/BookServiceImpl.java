package com.senla.training.yeutukhovich.bookstore.service.bookservice;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.TitleBookComparator;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private static BookServiceImpl instance;
    private static final int STALE_MONTH_NUMBER = 6;

    private IRepository<Book> bookRepository;
    private IRepository<Order> orderRepository;
    private IRepository<Request> requestRepository;

    private BookServiceImpl() {
        this.bookRepository = BookRepository.getInstance();
        this.orderRepository = OrderRepository.getInstance();
        this.requestRepository = RequestRepository.getInstance();
    }

    public static BookServiceImpl getInstance() {
        if (instance == null) {
            instance = new BookServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean replenishBook(Long id) {
        Book checkedBook = bookRepository.findById(id);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            checkedBook.setAvailable(true);
            checkedBook.setReplenishmentDate(new Date());
            bookRepository.update(checkedBook);
            closeRequests(checkedBook);
            updateOrders(checkedBook);
            return true;
        }
        return false;
    }

    @Override
    public boolean writeOffBook(Long id) {
        Book checkedBook = bookRepository.findById(id);
        if (checkedBook != null && checkedBook.isAvailable()) {
            checkedBook.setAvailable(false);
            bookRepository.update(checkedBook);
            updateOrders(checkedBook);
            return true;
        }
        return false;
    }

    @Override
    public List<Book> findAllBooks(Comparator<Book> bookComparator) {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .sorted(bookComparator)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .filter((order) -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(startDate)
                        && order.getCompletionDate().before(endDate))
                .map(Order::getBook)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        List<Book> soldBooks = findSoldBooksBetweenDates(startDate, endDate);
        List<Book> books = findAllBooks(TitleBookComparator.getInstance());
        return books.stream()
                .filter(book -> !soldBooks.contains(book))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findStaleBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -STALE_MONTH_NUMBER);
        Date currentDate = new Date();
        Date staleDate = new Date(calendar.getTimeInMillis());

        List<Book> unsoldBooks = findUnsoldBooksBetweenDates(staleDate, currentDate);
        return unsoldBooks.stream()
                .filter(book -> book.getReplenishmentDate() != null && book.getReplenishmentDate().before(staleDate))
                .collect(Collectors.toList());
    }

    @Override
    public BookDescription showBookDescription(Long id) {
        Book checkedBook = bookRepository.findById(id);
        BookDescription bookDescription = null;
        if (checkedBook != null) {
            bookDescription = new BookDescription();
            bookDescription.setTitle(checkedBook.getTitle());
            bookDescription.setEditionDate(checkedBook.getEditionDate());
            bookDescription.setReplenishmentDate(checkedBook.getReplenishmentDate());
        }
        return bookDescription;
    }

    private void closeRequests(Book book) {
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request != null && request.isActive() && request.getBook().getId().equals(book.getId())) {
                request.setActive(false);
                requestRepository.update(request);
            }
        }
    }

    private void updateOrders(Book book) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order != null && order.getState() == OrderState.CREATED &&
                    order.getBook().getId().equals(book.getId())) {
                order.setBook(book);
                orderRepository.update(order);
            }
        }
    }
}
