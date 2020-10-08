package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.DtoMapper;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private String cvsDirectoryPath = ApplicationConstant.CVS_DIRECTORY_PATH;

    @Override
    @Transactional
    public OrderDto createOrder(Long bookId, String customerData) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.CREATE_ORDER_FAIL.getMessage(), bookId,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
                });
        if (!book.isAvailable()) {
            Long requestId = requestDao.add(new Request(book, customerData));
            LOGGER.info(LoggerConstant.CREATE_REQUEST_SUCCESS.getMessage(), requestId, bookId);
        }
        Order order = new Order(book, customerData);
        orderDao.add(order);
        LOGGER.info(LoggerConstant.CREATE_ORDER_SUCCESS.getMessage(), order.getId(), bookId);
        return DtoMapper.mapOrder(order);
    }

    @Override
    @Transactional
    public OrderDto cancelOrder(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.CANCEL_ORDER_FAIL.getMessage(), orderId,
                            MessageConstant.ORDER_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
                });
        if (order.getState() != OrderState.CREATED) {
            LOGGER.warn(LoggerConstant.CANCEL_ORDER_FAIL.getMessage(), orderId,
                    MessageConstant.WRONG_ORDER_STATE.getMessage());
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.CANCELED);
        order.setCompletionDate(new Date());
        orderDao.update(order);
        LOGGER.info(LoggerConstant.CANCEL_ORDER_SUCCESS.getMessage(), orderId);
        return DtoMapper.mapOrder(order);
    }

    @Override
    @Transactional
    public OrderDto completeOrder(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.COMPLETE_ORDER_FAIL.getMessage(), orderId,
                            MessageConstant.ORDER_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
                });
        if (!order.getBook().isAvailable()) {
            LOGGER.warn(LoggerConstant.COMPLETE_ORDER_FAIL.getMessage(), orderId,
                    MessageConstant.BOOK_NOT_AVAILABLE.getMessage());
            throw new BusinessException(MessageConstant.BOOK_NOT_AVAILABLE.getMessage());
        }
        if (order.getState() != OrderState.CREATED) {
            LOGGER.warn(LoggerConstant.COMPLETE_ORDER_FAIL.getMessage(), orderId,
                    MessageConstant.WRONG_ORDER_STATE.getMessage());
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.COMPLETED);
        order.setCompletionDate(new Date());
        orderDao.update(order);
        LOGGER.info(LoggerConstant.COMPLETE_ORDER_SUCCESS.getMessage(), orderId);
        return DtoMapper.mapOrder(order);
    }

    @Override
    @Transactional
    public List<OrderDto> findSortedAllOrdersByCompletionDate() {
        LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_COMPLETION_DATE.getMessage());
        return orderDao.findSortedAllOrdersByCompletionDate().stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDto> findSortedAllOrdersByPrice() {
        LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_PRICE.getMessage());
        return orderDao.findSortedAllOrdersByPrice().stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDto> findSortedAllOrdersByState() {
        LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_STATE.getMessage());
        return orderDao.findSortedAllOrdersByState().stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDto> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.FIND_COMPLETED_ORDERS_BETWEEN_DATES.getMessage(),
                DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return orderDao.findCompletedOrdersBetweenDates(startDate, endDate).stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.SHOW_PROFIT_BETWEEN_DATES.getMessage(),
                DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return orderDao.calculateProfitBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional
    public Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        LOGGER.info(LoggerConstant.SHOW_COMPLETE_ORDERS_NUMBER_BETWEEN_DATES.getMessage(),
                DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
        return orderDao.calculateCompletedOrdersNumberBetweenDates(startDate, endDate);
    }

    @Override
    @Transactional
    public OrderDetailsDto showOrderDetails(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.SHOW_ORDER_DETAILS_FAIL.getMessage(), orderId,
                            MessageConstant.ORDER_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
                });
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setCustomerData(order.getCustomerData());
        orderDetailsDto.setBookTitle(order.getBook().getTitle());
        orderDetailsDto.setPrice(order.getCurrentBookPrice());
        orderDetailsDto.setState(order.getState().toString());
        orderDetailsDto.setCreationDate(order.getCreationDate());
        orderDetailsDto.setCompletionDate(order.getCompletionDate());
        LOGGER.info(LoggerConstant.SHOW_ORDER_DETAILS_SUCCESS.getMessage(), orderId);
        return orderDetailsDto;
    }

    @Override
    @Transactional
    public List<OrderDto> exportAllOrders(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        List<Order> orders = orderDao.findAll();
        List<String> orderStrings = entityCvsConverter.convertOrders(orders);
        FileDataWriter.writeData(path, orderStrings);
        LOGGER.info(LoggerConstant.EXPORT_ALL_ORDERS.getMessage(), fileName);
        return orders.stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDto exportOrder(Long id, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        Order order = orderDao.findById(id).orElseThrow(() -> {
            LOGGER.warn(LoggerConstant.EXPORT_ORDER_FAIL.getMessage(), id,
                    MessageConstant.ORDER_NOT_EXIST.getMessage());
            return new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        });
        List<String> orderStrings = entityCvsConverter.convertOrders(List.of(order));
        FileDataWriter.writeData(path, orderStrings);
        LOGGER.info(LoggerConstant.EXPORT_ORDER_SUCCESS.getMessage(), id, fileName);
        return DtoMapper.mapOrder(order);
    }

    @Override
    @Transactional
    public List<OrderDto> importOrders(String fileName) {
        List<String> orderStrings = readStringsFromFile(fileName);
        List<Order> importedOrders = entityCvsConverter.parseOrders(orderStrings);
        for (Order importedOrder : importedOrders) {
            Book dependentBook = bookDao.findById(importedOrder.getBook().getId())
                    .orElseThrow(() -> {
                        LOGGER.warn(LoggerConstant.IMPORT_ORDERS_FAIL.getMessage(),
                                MessageConstant.BOOK_CANT_NULL.getMessage());
                        return new BusinessException(MessageConstant.BOOK_CANT_NULL.getMessage());
                    });
            importedOrder.setBook(dependentBook);
            orderDao.update(importedOrder);
        }
        LOGGER.info(LoggerConstant.IMPORT_ORDERS_SUCCESS.getMessage(), fileName);
        return importedOrders.stream()
                .map(DtoMapper::mapOrder)
                .collect(Collectors.toList());
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
