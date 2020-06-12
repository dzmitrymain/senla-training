package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.BookService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.ReplenishmentDateBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.book.TitleBookComparator;
import com.senla.training.yeutukhovich.bookstore.util.comparator.order.CompletionDateOrderComparator;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class BookServiceImpl implements BookService {

    private static final int STALE_MONTH_NUMBER = 6;

    private EntityRepository<Book> bookRepository;
    private EntityRepository<Order> orderRepository;
    private EntityRepository<Request> requestRepository;

    public BookServiceImpl(EntityRepository<Book> bookRepository,
                           EntityRepository<Order> orderRepository,
                           EntityRepository<Request> requestRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public void replenishBook(Book book) {
        if (book != null) {
            Book checkedBook = bookRepository.findById(book.getId());
            if (checkedBook != null && !checkedBook.isAvailable()) {
                checkedBook.setAvailable(true);
                checkedBook.setReplenishmentDate(new Date());
                bookRepository.update(checkedBook);
                closeRequests(checkedBook);
                updateOrders(checkedBook);
                System.out.println("Book: {" + checkedBook.getTitle() + "} has been replenished. All requests has been closed.");
            }
        }
    }

    @Override
    public void writeOffBook(Book book) {
        if (book != null) {
            Book checkedBook = bookRepository.findById(book.getId());
            if (checkedBook != null && checkedBook.isAvailable()) {
                checkedBook.setAvailable(false);
                bookRepository.update(checkedBook);
                updateOrders(checkedBook);
                System.out.println("Book {" + checkedBook.getTitle() + "} has been written off.");
            }
        }
    }

    @Override
    public Book[] findAllBooks(Comparator<Book> bookComparator) {
        Book[] books = bookRepository.findAll();
        Arrays.sort(books, bookComparator);
        System.out.println("All books has been found.");
        return books;
    }

    @Override
    public Book[] findSoldBooksBetweenDates(Date startDate, Date endDate) {
        Order[] completedOrders = findCompletedOrdersBetweenDates(startDate, endDate, new CompletionDateOrderComparator());

        Book[] soldBooks = new Book[completedOrders.length];
        int uniqueSoldBooksNumber = 0;
        for (Order completedOrder : completedOrders) {
            boolean isBookUnique = true;
            for (Book book : soldBooks) {
                if (completedOrder.getBook().equals(book)) {
                    isBookUnique = false;
                    break;
                }
            }
            if (isBookUnique) {
                soldBooks[uniqueSoldBooksNumber++] = completedOrder.getBook();
            }
        }
        System.out.println("Sold books between dates has been found.");
        return Arrays.copyOf(soldBooks, uniqueSoldBooksNumber);
    }

    @Override
    public Book[] findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        Book[] soldBooks = findSoldBooksBetweenDates(startDate, endDate);
        Book[] books = findAllBooks(new TitleBookComparator());

        for (int i = 0, j = 0; i < books.length; i++) {
            while (j < soldBooks.length) {
                if (soldBooks[j].equals(books[i])) {
                    j++;
                    books[i++] = null;
                    break;
                }
            }
        }
        Arrays.sort(books, new ReplenishmentDateBookComparator());
        System.out.println("Unsold books between dates has been found.");
        return Arrays.copyOf(books, books.length - soldBooks.length);
    }

    @Override
    public Book[] findStaleBooks() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -STALE_MONTH_NUMBER);
        Date currentDate = new Date();
        Date staleDate = new Date(calendar.getTimeInMillis());

        Book[] unsoldBooks = findUnsoldBooksBetweenDates(staleDate, currentDate);

        Book[] staleBooks = new Book[unsoldBooks.length];
        int staleBooksNumber = 0;
        for (Book book : unsoldBooks) {
            if (book.getReplenishmentDate() != null && book.getReplenishmentDate().before(staleDate)) {
                staleBooks[staleBooksNumber++] = book;
            }
        }
        System.out.println("Stale books has been found.");
        return Arrays.copyOf(staleBooks, staleBooksNumber);
    }

    @Override
    public BookDescription showBookDescription(Book book) {
        Book checkedBook = bookRepository.findById(book.getId());
        BookDescription bookDescription = new BookDescription();
        if (checkedBook != null) {
            bookDescription.setTitle(checkedBook.toString());
            bookDescription.setEditionDate(checkedBook.getEditionDate());
        }
        System.out.println("Book description has been found.");
        return bookDescription;
    }

    private void closeRequests(Book book) {
        Request[] requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request != null && request.isActive() && request.getBook().getId().equals(book.getId())) {
                request.setActive(false);
                requestRepository.update(request);
                System.out.println("All book's requests {" + book.getTitle() + "} has been closed.");
            }
        }
    }

    private void updateOrders(Book book) {
        Order[] orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order != null && order.getState() == OrderState.CREATED && order.getBook().getId().equals(book.getId())) {
                order.setBook(book);
                orderRepository.update(order);
            }
        }
    }

    private Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate, Comparator<Order> orderComparator) {
        Order[] orders = orderRepository.findAll();
        Order[] desiredOrders = new Order[orders.length];

        int desiredOrdersNumber = 0;
        for (Order order : orders) {
            if (order.getState() == OrderState.COMPLETED && order.getCompletionDate().after(startDate)
                    && order.getCompletionDate().before(endDate)) {
                desiredOrders[desiredOrdersNumber++] = order;
            }
        }
        Arrays.sort(desiredOrders, orderComparator);
        System.out.println("Completed orders between dates has been found.");
        return Arrays.copyOf(desiredOrders, desiredOrdersNumber);
    }
}
