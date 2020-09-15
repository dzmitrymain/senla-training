package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @Value("${csv.directoryPath:resources/cvs/}")
    private String cvsDirectoryPath;

    @Transactional
    @Override
    public CreationOrderResult createOrder(Long bookId, String customerData) {
        CreationOrderResult result = new CreationOrderResult();
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        if (!book.isAvailable()) {
            result.setRequestId(requestDao.add(new Request(book, customerData)));
        }
        Order order = new Order(book, customerData);
        result.setOrderId(orderDao.add(order));
        return result;
    }

    @Transactional
    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage()));
        if (order.getState() != OrderState.CREATED) {
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.CANCELED);
        order.setCompletionDate(new Date());
        orderDao.update(order);
    }

    @Transactional
    @Override
    public void completeOrder(Long orderId) {
        Order order = orderDao.findById(orderId)
                .orElseThrow(() -> new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage()));
        if (!order.getBook().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_AVAILABLE.getMessage());
        }
        if (order.getState() != OrderState.CREATED) {
            throw new BusinessException(MessageConstant.WRONG_ORDER_STATE.getMessage());
        }
        order.setState(OrderState.COMPLETED);
        order.setCompletionDate(new Date());
        orderDao.update(order);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> findSortedAllOrdersByCompletionDate() {
        return orderDao.findSortedAllOrdersByCompletionDate();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> findSortedAllOrdersByPrice() {
        return orderDao.findSortedAllOrdersByPrice();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> findSortedAllOrdersByState() {
        return orderDao.findSortedAllOrdersByState();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        return orderDao.findCompletedOrdersBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        return orderDao.calculateProfitBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        return orderDao.calculateCompletedOrdersNumberBetweenDates(startDate, endDate);
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDetails showOrderDetails(Long orderId) {
        Order order = orderDao.findById(orderId)
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

    @Transactional(readOnly = true)
    @Override
    public int exportAllOrders(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        List<String> orderStrings = entityCvsConverter.convertOrders(orderDao.findAll());
        return FileDataWriter.writeData(path, orderStrings);
    }

    @Transactional(readOnly = true)
    @Override
    public void exportOrder(Long id, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        Optional<Order> orderOptional = orderDao.findById(id);
        if (orderOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.ORDER_NOT_EXIST.getMessage());
        }
        Order order = orderOptional.get();
        List<String> orderStrings = entityCvsConverter.convertOrders(List.of(order));
        FileDataWriter.writeData(path, orderStrings);
    }

    @Transactional
    @Override
    public int importOrders(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedOrdersNumber = 0;
        List<String> orderStrings = readStringsFromFile(fileName);
        List<Order> importedOrders = entityCvsConverter.parseOrders(orderStrings);
        for (Order importedOrder : importedOrders) {
            Book dependentBook = bookDao.findById(importedOrder.getBook().getId())
                    .orElseThrow(() -> new BusinessException("Book can't be null."));
            importedOrder.setBook(dependentBook);
            orderDao.update(importedOrder);
            importedOrdersNumber++;
        }
        return importedOrdersNumber;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
