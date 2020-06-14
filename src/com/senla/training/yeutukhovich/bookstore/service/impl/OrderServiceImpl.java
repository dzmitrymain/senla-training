package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class OrderServiceImpl implements OrderService {

    private static OrderServiceImpl instance;

    private EntityRepository<Book> bookRepository;
    private EntityRepository<Order> orderRepository;
    private EntityRepository<Request> requestRepository;

    private OrderServiceImpl(EntityRepository<Book> bookRepository,
                             EntityRepository<Order> orderRepository,
                             EntityRepository<Request> requestRepository) {
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.requestRepository = requestRepository;
    }

    public static OrderServiceImpl getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl(
                    EntityRepository.getBookRepositoryInstance(),
                    EntityRepository.getOrderRepositoryInstance(),
                    EntityRepository.getRequestRepositoryInstance());
        }
        return instance;
    }

    @Override
    public Order createOrder(Long bookId, String customerData) {
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null) {
            if (!checkedBook.isAvailable()) {
                createRequest(checkedBook, customerData);
            }
            Order order = new Order(checkedBook, customerData);
            orderRepository.add(order);
            System.out.println("Order {" + checkedBook.getTitle() + "} has been created.");
            return order;
        }
        return null;
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order checkedOrder = orderRepository.findById(orderId);
        if (checkedOrder != null && checkedOrder.getState() == OrderState.CREATED) {
            checkedOrder.setState(OrderState.CANCELED);
            orderRepository.update(checkedOrder);
            System.out.println("Order {" + checkedOrder.getBook().getTitle() + "} has been canceled.");
        }
    }

    @Override
    public void completeOrder(Long orderId) {
        Order checkedOrder = orderRepository.findById(orderId);
        if (checkedOrder != null && checkedOrder.getBook().isAvailable() && checkedOrder.getState() == OrderState.CREATED) {
            checkedOrder.setState(OrderState.COMPLETED);
            checkedOrder.setCompletionDate(new Date());
            orderRepository.update(checkedOrder);
            System.out.println("Order {" + checkedOrder.getBook().getTitle() + "} has been completed.");
        }
    }

    @Override
    public Order[] findAllOrders(Comparator<Order> orderComparator) {
        Order[] orders = orderRepository.findAll();
        Arrays.sort(orders, orderComparator);
        System.out.println("All orders has been found.");
        return orders;
    }

    @Override
    public Order[] findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        Order[] orders = orderRepository.findAll();
        Order[] desiredOrders = new Order[orders.length];

        int desiredOrdersNumber = 0;
        for (Order order : orders) {
            if (order.getState() == OrderState.COMPLETED && order.getCompletionDate().after(startDate)
                    && order.getCompletionDate().before(endDate)) {
                desiredOrders[desiredOrdersNumber++] = order;
            }
        }
        System.out.println("Completed orders between dates has been found.");
        return Arrays.copyOf(desiredOrders, desiredOrdersNumber);
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        Order[] completedOrders = findCompletedOrdersBetweenDates(startDate, endDate);

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
        return findCompletedOrdersBetweenDates(startDate, endDate).length;
    }

    @Override
    public OrderDetails showOrderDetails(Long orderId) {
        Order checkedOrder = orderRepository.findById(orderId);
        OrderDetails orderDetails = null;
        if (checkedOrder != null) {
            orderDetails = new OrderDetails();
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

    private void createRequest(Book book, String requesterData) {
        if (book != null) {
            Book checkedBook = bookRepository.findById(book.getId());
            if (checkedBook != null && !checkedBook.isAvailable()) {
                Request request = new Request(checkedBook, requesterData);
                requestRepository.add(request);
                System.out.println("Request {" + checkedBook.getTitle() + "} has been created.");
            }
        }
    }
}
