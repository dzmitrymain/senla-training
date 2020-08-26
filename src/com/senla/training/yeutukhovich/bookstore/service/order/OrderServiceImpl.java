package com.senla.training.yeutukhovich.bookstore.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.dao.connector.DbConnector;
import com.senla.training.yeutukhovich.bookstore.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class OrderServiceImpl implements OrderService {

    @Autowired
    private DbConnector connector;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Override
    public CreationOrderResult createOrder(Long bookId, String customerData) {
        Connection connection = connector.getConnection();
        CreationOrderResult result = new CreationOrderResult();
        Book book = bookDao.findById(connection, bookId)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        try {
            connection.setAutoCommit(false);
            try {
                if (!book.isAvailable()) {
                    result.setRequestId(requestDao.add(connection, new Request(book, customerData)));
                }
                Order order = new Order(book, customerData);
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
        Order order = orderDao.findById(connection, orderId)
                .orElseThrow(() -> new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage()));
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
        Order order = orderDao.findById(connection, orderId)
                .orElseThrow(() -> new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage()));
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
                .sorted(Comparator.nullsLast(Comparator.comparing(Order::getCurrentBookPrice)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findSortedAllOrdersByState() {
        return findAllOrders().stream()
                .sorted(Comparator.nullsLast(Comparator.comparing(Order::getState)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        return orderDao.findCompletedOrdersBetweenDates(connector.getConnection(), startDate, endDate);
    }

    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        List<Order> completedOrders = orderDao.findCompletedOrdersBetweenDates(connector.getConnection(),
                startDate, endDate);
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
        Connection connection = connector.getConnection();
        Order order = orderDao.findById(connection, orderId)
                .orElseThrow(() -> new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage()));
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setCustomerData(order.getCustomerData());
        orderDetails.setBookTitle(order.getBook().getTitle());
        orderDetails.setPrice(order.getCurrentBookPrice());
        orderDetails.setState(order.getState());
        orderDetails.setCreationDate(order.getCreationDate());
        orderDetails.setCompletionDate(order.getCompletionDate());
        return orderDetails;
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

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        return FileDataReader.readData(path);
    }
}
