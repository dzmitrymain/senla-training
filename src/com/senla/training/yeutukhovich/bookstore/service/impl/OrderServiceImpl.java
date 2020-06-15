package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.OrderService;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private static OrderServiceImpl instance;

    private EntityRepository<Book> bookRepository;
    private EntityRepository<Order> orderRepository;
    private EntityRepository<Request> requestRepository;

    private OrderServiceImpl() {
        this.bookRepository = EntityRepository.getBookRepositoryInstance();
        this.orderRepository = EntityRepository.getOrderRepositoryInstance();
        this.requestRepository = EntityRepository.getRequestRepositoryInstance();
    }

    public static OrderServiceImpl getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

    @Override
    public Order createOrder(Long bookId, String customerData) {
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null) {
            if (!checkedBook.isAvailable()) {
                createRequest(bookId, customerData);
            }
            Order order = new Order(checkedBook, customerData);
            orderRepository.add(order);
            return order;
        }
        return null;
    }

    @Override
    public boolean cancelOrder(Long orderId) {
        Order checkedOrder = orderRepository.findById(orderId);
        if (checkedOrder != null && checkedOrder.getState() == OrderState.CREATED) {
            checkedOrder.setState(OrderState.CANCELED);
            orderRepository.update(checkedOrder);
            return true;
        }
        return false;
    }

    @Override
    public boolean completeOrder(Long orderId) {
        Order checkedOrder = orderRepository.findById(orderId);
        if (checkedOrder != null && checkedOrder.getBook().isAvailable() &&
                checkedOrder.getState() == OrderState.CREATED) {
            checkedOrder.setState(OrderState.COMPLETED);
            checkedOrder.setCompletionDate(new Date());
            orderRepository.update(checkedOrder);
            return true;
        }
        return false;
    }

    @Override
    public List<Order> findAllOrders(Comparator<Order> orderComparator) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().sorted(orderComparator).collect(Collectors.toList());
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().filter((order) -> order.getState() == OrderState.COMPLETED &&
                order.getCompletionDate().after(startDate) &&
                order.getCompletionDate().before(endDate)).collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        List<Order> completedOrders = findCompletedOrdersBetweenDates(startDate, endDate);
        BigDecimal profit = new BigDecimal(0);
        for (Order order : completedOrders) {
            profit = profit.add(order.getCurrentBookPrice());
        }
        return profit;
    }

    @Override
    public int calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        return findCompletedOrdersBetweenDates(startDate, endDate).size();
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
            orderDetails.setCompletionDate(checkedOrder.getCompletionDate());
        }
        return orderDetails;
    }

    private void createRequest(Long bookId, String requesterData) {
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            Request request = new Request(checkedBook, requesterData);
            requestRepository.add(request);
        }
    }
}
