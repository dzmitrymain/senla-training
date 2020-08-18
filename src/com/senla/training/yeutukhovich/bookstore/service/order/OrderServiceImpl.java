package com.senla.training.yeutukhovich.bookstore.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.service.AbstractService;
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
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class OrderServiceImpl extends AbstractService implements OrderService {

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private OrderServiceImpl() {

    }

    @Override
    public CreationOrderResult createOrder(Long bookId, String customerData) {
        CreationOrderResult result = new CreationOrderResult();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        if (!bookOptional.get().isAvailable()) {
            result.setRequestId(createRequest(bookId, customerData));
        }
        Order order = new Order(bookOptional.get(), customerData);
        orderRepository.add(order);
        result.setOrderId(order.getId());
        return result;
    }

    @Override
    public void cancelOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        if (order.getState() != OrderState.CREATED) {
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.CANCELED);
        orderRepository.update(order);
    }

    @Override
    public void completeOrder(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
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
        orderRepository.update(order);
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
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED &&
                        order.getCompletionDate().after(startDate) &&
                        order.getCompletionDate().before(endDate))
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
    public Optional<OrderDetails> showOrderDetails(Long orderId) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
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
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> orderStrings = entityCvsConverter.convertOrders(orderRepository.findAll());
        return FileDataWriter.writeData(path, orderStrings);
    }

    @Override
    public void exportOrder(Long id, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Order> orderOptional = orderRepository.findById(id);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        List<String> orderStrings = entityCvsConverter.convertOrders(List.of(order));
        FileDataWriter.writeData(path, orderStrings);
    }

    @Override
    public int importOrders(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedOrdersNumber = 0;
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> orderStrings = FileDataReader.readData(path);

        List<Order> repoOrders = orderRepository.findAll();
        List<Order> importedOrders = entityCvsConverter.parseOrders(orderStrings);

        for (Order importedOrder : importedOrders) {
            Optional<Book> dependentBookOptional = bookRepository.findById(importedOrder.getBook().getId());
            if (dependentBookOptional.isEmpty()) {
                //log
                //System.err.println(MessageConstant.BOOK_NOT_NULL.getMessage());
                continue;
            }
            importedOrder.setBook(dependentBookOptional.get());
            if (repoOrders.contains(importedOrder)) {
                orderRepository.update(importedOrder);
            } else {
                orderRepository.add(importedOrder);
            }
            importedOrdersNumber++;
        }
        return importedOrdersNumber;
    }

    private List<Order> findAllOrders() {
        return orderRepository.findAll();
    }

    private Long createRequest(Long bookId, String requesterData) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty() || bookOptional.get().isAvailable()) {
            return null;
        }
        Request request = new Request(bookOptional.get(), requesterData);
        requestRepository.add(request);
        return request.getId();
    }
}
