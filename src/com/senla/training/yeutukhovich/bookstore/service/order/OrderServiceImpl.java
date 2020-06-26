package com.senla.training.yeutukhovich.bookstore.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.*;
import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.util.constant.PathConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private static OrderService instance;

    private IBookRepository bookRepository;
    private IOrderRepository orderRepository;
    private IRequestRepository requestRepository;

    private OrderServiceImpl() {
        this.bookRepository = BookRepository.getInstance();
        this.orderRepository = OrderRepository.getInstance();
        this.requestRepository = RequestRepository.getInstance();
    }

    public static OrderService getInstance() {
        if (instance == null) {
            instance = new OrderServiceImpl();
        }
        return instance;
    }

    @Override
    public CreationOrderResult createOrder(Long bookId, String customerData) {
        CreationOrderResult result = new CreationOrderResult();
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null) {
            if (!checkedBook.isAvailable()) {
                result.setRequestId(createRequest(bookId, customerData));
            }
            Order order = new Order(checkedBook, customerData);
            orderRepository.add(order);
            result.setOrderId(order.getId());
        }
        return result;
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
    public List<String> findSortedAllOrdersByCompletionDate() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> {
                    if (o1.getCompletionDate() == null && o2.getCompletionDate() == null) {
                        return 0;
                    } else if (o1.getCompletionDate() == null) {
                        return 1;
                    } else if (o2.getCompletionDate() == null) {
                        return -1;
                    } else {
                        return o2.getCompletionDate().compareTo(o1.getCompletionDate());
                    }
                }))
                .filter(Objects::nonNull)
                .map(Order::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findSortedAllOrdersByPrice() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> o1.getCurrentBookPrice().compareTo(o2.getCurrentBookPrice())))
                .filter(Objects::nonNull)
                .map(Order::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findSortedAllOrdersByState() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> o1.getState().compareTo(o2.getState())))
                .filter(Objects::nonNull)
                .map(Order::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED &&
                        order.getCompletionDate().after(startDate) &&
                        order.getCompletionDate().before(endDate))
                .map(Order::toString)
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();
        List<Order> completedOrders = orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED &&
                        order.getCompletionDate().after(startDate) &&
                        order.getCompletionDate().before(endDate))
                .collect(Collectors.toList());
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
        if (checkedOrder == null) {
            return null;
        }
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerData(checkedOrder.getCustomerData());
        orderDetails.setBookTitle(checkedOrder.getBook().getTitle());
        orderDetails.setPrice(checkedOrder.getCurrentBookPrice());
        orderDetails.setState(checkedOrder.getState());
        orderDetails.setCreationDate(checkedOrder.getCreationDate());
        orderDetails.setCompletionDate(checkedOrder.getCompletionDate());
        return orderDetails;
    }

    @Override
    public int exportAllOrders(String fileName) {
        int exportedOrdersNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            List<String> orderStrings = EntityCvsConverter.getInstance().convertOrders(orderRepository.findAll());
            exportedOrdersNumber = FileDataWriter.writeData(path, orderStrings);
        }
        return exportedOrdersNumber;
    }

    @Override
    public boolean exportOrder(Long id, String fileName) {
        if (id != null && fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            Order order = orderRepository.findById(id);
            if (order != null) {
                List<String> orderStrings = EntityCvsConverter.getInstance().convertOrders(List.of(order));
                return FileDataWriter.writeData(path, orderStrings) != 0;
            }
        }
        return false;
    }

    @Override
    public int importOrders(String fileName) {
        int importedOrdersNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            List<String> orderStrings = FileDataReader.readData(path);

            List<Order> repoOrders = orderRepository.findAll();
            List<Order> importedOrders = EntityCvsConverter.getInstance().parseOrders(orderStrings);

            for (Order importedOrder : importedOrders) {
                if (repoOrders.contains(importedOrder)) {
                    orderRepository.update(importedOrder);
                } else {
                    orderRepository.add(importedOrder);
                }
                importedOrdersNumber++;
            }
        }
        return importedOrdersNumber;
    }

    private List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    private Long createRequest(Long bookId, String requesterData) {
        Long requestId = null;
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            Request request = new Request(checkedBook, requesterData);
            requestRepository.add(request);
            requestId = request.getId();
        }
        return requestId;
    }
}
