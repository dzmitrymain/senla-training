package com.senla.training.yeutukhovich.bookstore.service.order;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.AbstractService;
import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

//TODO: add DAO SELECT methods
@Singleton
public class OrderServiceImpl extends AbstractService implements OrderService {

    private OrderServiceImpl() {

    }

    @Override
    public CreationOrderResult createOrder(Long bookId, String customerData) {
        Connection connection = connector.getConnection();
        CreationOrderResult result = new CreationOrderResult();
        Optional<Book> bookOptional = bookDao.findById(connection, bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        try {
            connection.setAutoCommit(false);
            try {
                if (!bookOptional.get().isAvailable()) {
                    result.setRequestId(createRequest(connection, bookOptional.get(), customerData));
                }
                Order order = new Order(bookOptional.get(), customerData);
                orderDao.add(connection, order);
                connection.commit();
                result.setOrderId(order.getId());
                return result;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public void cancelOrder(Long orderId) {
        Connection connection = connector.getConnection();
        Optional<Order> orderOptional = orderDao.findById(connection, orderId);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        if (order.getState() != OrderState.CREATED) {
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.CANCELED);
        order.setCompletionDate(new Date());
        orderDao.update(connection, order);
    }

    @Override
    public void completeOrder(Long orderId) {
        Connection connection = connector.getConnection();
        Optional<Order> orderOptional = orderDao.findById(connection, orderId);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        if (!order.getBook().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_AVAILABLE.getMessage());
        }
        if (order.getState() != OrderState.CREATED) {
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.COMPLETED);
        order.setCompletionDate(new Date());
        orderDao.update(connection, order);
    }

    @Override
    public List<Order> findSortedAllOrdersByCompletionDate() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> {
                    if (o1.getCompletionDate() == null && o2.getCompletionDate() == null) {
                        return 0;
                    }
                    if (o1.getCompletionDate() == null) {
                        return 1;
                    }
                    if (o2.getCompletionDate() == null) {
                        return -1;
                    }
                    return o2.getCompletionDate().compareTo(o1.getCompletionDate());
                }))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findSortedAllOrdersByPrice() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> o1.getCurrentBookPrice().compareTo(o2.getCurrentBookPrice())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findSortedAllOrdersByState() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast((o1, o2) -> o1.getState().compareTo(o2.getState())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        Connection connection = connector.getConnection();
        List<Order> orders = orderDao.findAll(connection);
        return orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED &&
                        order.getCompletionDate().after(startDate) &&
                        order.getCompletionDate().before(endDate))
                .collect(Collectors.toList());
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        Connection connection = connector.getConnection();
        List<Order> orders = orderDao.findAll(connection);
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
    public Optional<OrderDetails> showOrderDetails(Long orderId) {
        Connection connection = connector.getConnection();
        Optional<Order> orderOptional = orderDao.findById(connection, orderId);
        if (orderOptional.isEmpty()) {
            return Optional.empty();
        }
        Order order = orderOptional.get();
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerData(order.getCustomerData());
        orderDetails.setBookTitle(order.getBook().getTitle());
        orderDetails.setPrice(order.getCurrentBookPrice());
        orderDetails.setState(order.getState());
        orderDetails.setCreationDate(order.getCreationDate());
        orderDetails.setCompletionDate(order.getCompletionDate());
        return Optional.of(orderDetails);
    }

    @Override
    public int exportAllOrders(String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> orderStrings = entityCvsConverter.convertOrders(orderDao.findAll(connection));
        return FileDataWriter.writeData(path, orderStrings);
    }

    @Override
    public void exportOrder(Long id, String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Order> orderOptional = orderDao.findById(connection, id);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        List<String> orderStrings = entityCvsConverter.convertOrders(List.of(order));
        FileDataWriter.writeData(path, orderStrings);
    }

    @Override
    public int importOrders(String fileName) {
        Connection connection = connector.getConnection();
        if (fileName == null) {
            return 0;
        }
        int importedOrdersNumber = 0;
        List<String> orderStrings = readStringsFromFile(fileName);
        List<Order> repoOrders = orderDao.findAll(connection);
        List<Order> importedOrders = entityCvsConverter.parseOrders(orderStrings);
        try {
            connection.setAutoCommit(false);
            try {
                for (Order importedOrder : importedOrders) {
                    Optional<Book> dependentBookOptional = bookDao.findById(connection, importedOrder.getBook().getId());
                    if (dependentBookOptional.isEmpty()) {
                        //log(MessageConstant.BOOK_NOT_NULL.getMessage());
                        continue;
                    }
                    importedOrder.setBook(dependentBookOptional.get());
                    if (repoOrders.contains(importedOrder)) {
                        orderDao.update(connection, importedOrder);
                    } else {
                        orderDao.add(connection, importedOrder);
                    }
                    importedOrdersNumber++;
                }
                connection.commit();
                return importedOrdersNumber;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    private List<Order> findAllOrders() {
        Connection connection = connector.getConnection();
        return orderDao.findAll(connection);
    }

    private Long createRequest(Connection connection, Book book, String requesterData) {
        Request request = new Request(book, requesterData);
        return requestDao.add(connection, request);
    }
}
