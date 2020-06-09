package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.book.ReplenishmentDateBookComparator;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.book.TitleBookComparator;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.order.CompletionDateOrderComparator;
import com.senla.training.yeutukhovich.bookstore.domain.comparator.order.PriceOrderComparator;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.BookstoreService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

public class BookstoreServiceImpl implements BookstoreService {

    private EntityRepository bookRepository;
    private EntityRepository orderRepository;
    private EntityRepository requestRepository;

    public BookstoreServiceImpl(Book[] books, Order[] orders, Request[] requests) {
        this.bookRepository = new EntityRepository(books);
        this.orderRepository = new EntityRepository(orders);
        this.requestRepository = new EntityRepository(requests);
    }

    @Override
    public void replenishBook(Book book) {
        if (book != null) { // а если равен нулю? можно что-то напечатать
            // от кастования избавиться очень просто - для каждой энтити свой репозиторий (не объект, а класс)
            // второй вариант, который можно уже заиспользовать - дженерики
            Book checkedBook = (Book) bookRepository.findById(book.getId());
            if (checkedBook != null && !checkedBook.isAvailable()) {
                checkedBook.setAvailable(true);
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
            Book checkedBook = (Book) bookRepository.findById(book.getId());
            if (checkedBook != null && checkedBook.isAvailable()) {
                checkedBook.setAvailable(false);
                bookRepository.update(checkedBook);
                updateOrders(checkedBook);
                System.out.println("Book {" + checkedBook.getTitle() + "} has been written off.");
            }
        }
    }

    @Override
    public Order createOrder(Book book, String customerData) {
        if (book != null) {
            Book checkedBook = (Book) bookRepository.findById(book.getId());
            if (checkedBook != null) {
                if (!checkedBook.isAvailable()) {
                    createRequest(checkedBook, customerData);
                }
                Order order = new Order(checkedBook, customerData);
                orderRepository.add(order);
                System.out.println("Order {" + checkedBook.getTitle() + "} has been created.");
                return order;
            }
        }
        return null;
    }

    @Override
    public void cancelOrder(Order order) {
        if (order != null) {
            Order checkedOrder = (Order) orderRepository.findById(order.getId());
            if (checkedOrder != null && checkedOrder.getState() == OrderState.CREATED) {
                checkedOrder.setState(OrderState.CANCELED);
                orderRepository.update(checkedOrder);
                System.out.println("Order {" + checkedOrder.getBook().getTitle() + "} has been canceled.");
            }
        }
    }

    @Override
    public void completeOrder(Order order) {
        if (order != null) {
            Order checkedOrder = (Order) orderRepository.findById(order.getId());
            if (checkedOrder != null && checkedOrder.getBook().isAvailable() && checkedOrder.getState() == OrderState.CREATED) {
                checkedOrder.setState(OrderState.COMPLETED);
                orderRepository.update(checkedOrder);
                System.out.println("Order {" + checkedOrder.getBook().getTitle() + "} has been completed.");
            }
        }
    }

    @Override
    public Request createRequest(Book book, String requesterData) {
        if (book != null) {
            Book checkedBook = (Book) bookRepository.findById(book.getId());
            if (checkedBook != null && !checkedBook.isAvailable()) {
                Request request = new Request(checkedBook, requesterData);
                requestRepository.add(request);
                System.out.println("Request {" + checkedBook.getTitle() + "} has been created.");
                return request;
            }
        }
        return null;
    }

    @Override
    public Book[] findAllBooks(Comparator<Book> bookComparator) {
        Book[] books = (Book[]) bookRepository.findAll();
        Arrays.sort(books, bookComparator);
        System.out.println("All books has been found.");
        return books;
    }

    @Override
    public Order[] findAllOrders(Comparator<Order> orderComparator) {
        Order[] orders = (Order[]) orderRepository.findAll();
        Arrays.sort(orders, orderComparator);
        System.out.println("All orders has been found.");
        return orders;
    }

    @Override
    public Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate, Comparator<Order> orderComparator) {
        Order[] orders = (Order[]) orderRepository.findAll();
        Order[] desiredOrders = new Order[orders.length];

        int desiredOrdersNumber = 0;
        for (Order order : orders) {
            // следим за длиной строки
            if (order.getState() == OrderState.COMPLETED && order.getCompletionDate().after(startDate) && order.getCompletionDate().before(endDate)) {
                desiredOrders[desiredOrdersNumber++] = order;
            }
        }
        Arrays.sort(desiredOrders, orderComparator);
        System.out.println("Completed orders between dates has been found.");
        return Arrays.copyOf(desiredOrders, desiredOrdersNumber);
    }

    @Override
    public Request[] findBookRequests(Book book, Comparator<Request> requestComparator) {
        Request[] requests = (Request[]) requestRepository.findAll();
        Arrays.sort(requests, requestComparator);
        System.out.println("Book requests {" + book.getTitle() + "} has been found.");
        return (Request[]) requestRepository.findAll();
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        Order[] completedOrders = findCompletedOrdersBetweenDates(startDate, endDate, new PriceOrderComparator());

        BigDecimal profit = new BigDecimal(0);
        for (Order order : completedOrders) {
            profit = profit.add(order.getCurrentBookPrice());
        }
        System.out.println("Profit between dates has been calculated.");
        return profit;
    }

    @Override
    public int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        System.out.println("Completed orders number has been calculated.");
        return findCompletedOrdersBetweenDates(startDate, endDate, new PriceOrderComparator()).length;
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
            // этот фор больше похож на while
            for (; j < soldBooks.length; ) {
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
        // такие литералы надо выносить в константы
        final int staleMonthNumber = 6;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
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
    public OrderDetails showOrderDetails(Order order) {
        Order checkedOrder = (Order) orderRepository.findById(order.getId());
        OrderDetails orderDetails = new OrderDetails();
        if (checkedOrder != null) {
            orderDetails.setCustomerData(checkedOrder.getCustomerData());
            orderDetails.setBookTitle(checkedOrder.getBook().getTitle());
            orderDetails.setPrice(checkedOrder.getCurrentBookPrice());
            orderDetails.setState(checkedOrder.getState());
            orderDetails.setCreationDate(checkedOrder.getCreationDate());
            if (checkedOrder.getState() == OrderState.COMPLETED) {
                orderDetails.setCompletionDate(checkedOrder.getCompletionDate());
            }
        }
        System.out.println("Order details has been found.");
        return orderDetails;
    }

    @Override
    public BookDescription showBookDescription(Book book) {
        Book checkedBook = (Book) bookRepository.findById(book.getId());
        BookDescription bookDescription = new BookDescription();
        if (checkedBook != null) {
            bookDescription.setTitle(checkedBook.toString());
            bookDescription.setEditionDate(checkedBook.getEditionDate());
        }
        System.out.println("Book description has been found.");
        return bookDescription;
    }

    private void closeRequests(Book book) {
        Request[] requests = (Request[]) requestRepository.findAll();
        for (Request request : requests) {
            if (request != null && request.isActive() && request.getBook().getId() == book.getId()) {
                request.setActive(false);
                requestRepository.update(request);
                System.out.println("All book's requests {" + book.getTitle() + "} has been closed.");
            }
        }
    }

    private void updateOrders(Book book) {
        Order[] orders = (Order[]) orderRepository.findAll();
        for (Order order : orders) {
            if (order != null && order.getState() == OrderState.CREATED && order.getBook().getId() == book.getId()) {
                order.setBook(book);
                orderRepository.update(order);
            }
        }
    }
}
