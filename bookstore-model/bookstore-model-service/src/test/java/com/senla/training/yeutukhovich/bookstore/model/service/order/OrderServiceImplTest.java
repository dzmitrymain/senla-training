package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.model.service.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class OrderServiceImplTest {

    private static final Book book = Mockito.mock(Book.class);
    private static final Order order = Mockito.mock(Order.class);
    private static final Long bookId = 1L;
    private static final Long orderId = 1L;

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @BeforeAll
    static void setup() {
        Mockito.when(order.getId()).thenReturn(orderId);
        Mockito.when(book.getId()).thenReturn(bookId);
        Mockito.when(order.getBook()).thenReturn(book);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(bookDao);
        Mockito.reset(orderDao);
        Mockito.reset(requestDao);
        Mockito.reset(entityCvsConverter);
    }

    @Test
    void OrderServiceImpl_createOrder_shouldCreateOrderAndRequestIfBookNotAvailable() {
        Mockito.when(book.isAvailable()).thenReturn(false);
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        Order result = orderService.createOrder(bookId, "");

        Mockito.verify(requestDao, Mockito.times(1)).add(Mockito.any());
        Mockito.verify(orderDao, Mockito.times(1)).add(Mockito.any());
        Assertions.assertNotNull(result);
    }

    @Test
    void OrderServiceImpl_createOrder_shouldCreateOrderWithoutRequestIfBookAvailable() {
        Mockito.when(book.isAvailable()).thenReturn(true);
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        Order result = orderService.createOrder(bookId, "");

        Mockito.verify(requestDao, Mockito.never()).add(Mockito.any());
        Mockito.verify(orderDao, Mockito.times(1)).add(Mockito.any());
        Assertions.assertNotNull(result);
    }

    @Test
    void OrderServiceImpl_createOrder_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class,
                () -> orderService.createOrder(bookId, ""));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_cancelOrder_shouldUpdateIfOrderExistAndStateCreated() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(order.getState()).thenReturn(OrderState.CREATED);

        orderService.cancelOrder(orderId);

        Mockito.verify(orderDao, Mockito.times(1)).update(order);
    }

    @Test
    void OrderServiceImpl_cancelOrder_shouldThrowExceptionIfOrderNotExist() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.cancelOrder(orderId));

        Assertions.assertEquals(MessageConstant.ORDER_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_cancelOrder_shouldThrowExceptionIfStateNotCreated() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(order.getState()).thenReturn(null);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.cancelOrder(orderId));

        Assertions.assertEquals(MessageConstant.WRONG_ORDER_STATE.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_completeOrder_shouldUpdateIfOrderExistAndStateCreatedAndBookAvailable() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(order.getState()).thenReturn(OrderState.CREATED);
        Mockito.when(book.isAvailable()).thenReturn(true);

        orderService.completeOrder(orderId);

        Mockito.verify(orderDao, Mockito.times(1)).update(order);
    }

    @Test
    void OrderServiceImpl_completeOrder_shouldThrowExceptionIfOrderNotExist() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.completeOrder(orderId));

        Assertions.assertEquals(MessageConstant.ORDER_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_completeOrder_shouldThrowExceptionIfOrderStateNotCreated() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(book.isAvailable()).thenReturn(true);
        Mockito.when(order.getState()).thenReturn(null);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.completeOrder(orderId));

        Assertions.assertEquals(MessageConstant.WRONG_ORDER_STATE.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_completeOrder_shouldThrowExceptionIfBookNotAvailable() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(book.isAvailable()).thenReturn(false);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.completeOrder(orderId));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_AVAILABLE.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_findSortedAllOrdersByCompletionDate() {
        orderService.findSortedAllOrdersByCompletionDate();

        Mockito.verify(orderDao, Mockito.times(1)).findSortedAllOrdersByCompletionDate();
    }

    @Test
    void OrderServiceImpl_findSortedAllOrdersByPrice() {
        orderService.findSortedAllOrdersByPrice();

        Mockito.verify(orderDao, Mockito.times(1)).findSortedAllOrdersByPrice();
    }

    @Test
    void OrderServiceImpl_findSortedAllOrdersByState() {
        orderService.findSortedAllOrdersByState();

        Mockito.verify(orderDao, Mockito.times(1)).findSortedAllOrdersByState();
    }

    @Test
    void OrderServiceImpl_findCompletedOrdersBetweenDates() {
        orderService.findCompletedOrdersBetweenDates(Mockito.mock(Date.class), Mockito.mock(Date.class));

        Mockito.verify(orderDao,
                Mockito.times(1)).findCompletedOrdersBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void OrderServiceImpl_calculateProfitBetweenDates() {
        orderService.calculateProfitBetweenDates(Mockito.mock(Date.class), Mockito.mock(Date.class));

        Mockito.verify(orderDao,
                Mockito.times(1)).calculateProfitBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void OrderServiceImpl_calculateCompletedOrdersNumberBetweenDates() {
        orderService.calculateCompletedOrdersNumberBetweenDates(Mockito.mock(Date.class), Mockito.mock(Date.class));

        Mockito.verify(orderDao,
                Mockito.times(1)).calculateCompletedOrdersNumberBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void OrderServiceImpl_showOrderDetails_shouldReturnNotNull() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

        OrderDetailsDto result = orderService.showOrderDetails(orderId);

        Assertions.assertNotNull(result);
    }

    @Test
    void OrderServiceImpl_showOrderDetails_shouldThrowExceptionIfOrderNotExist() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.showOrderDetails(orderId));

        Assertions.assertEquals(MessageConstant.ORDER_NOT_EXIST.getMessage(), exception.getMessage());
    }


    @Test
    void OrderServiceImpl_exportAllOrders() {
        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Assertions.assertNotNull(orderService.exportAllOrders(""));
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertOrders(Mockito.anyList());
        Mockito.verify(orderDao, Mockito.times(1)).findAll();
    }

    @Test
    void OrderServiceImpl_exportOrder() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            orderService.exportOrder(orderId, "");
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertOrders(Mockito.anyList());
        Mockito.verify(orderDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void OrderServiceImpl_exportOrder_shouldThrowExceptionIfOrderNotExist() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Throwable exception = Assertions.assertThrows(BusinessException.class,
                    () -> orderService.exportOrder(orderId, ""));

            Assertions.assertEquals(MessageConstant.ORDER_NOT_EXIST.getMessage(), exception.getMessage());
        }
    }

    @Test
    void OrderServiceImpl_importOrders() {
        Mockito.when(entityCvsConverter.parseOrders(Mockito.anyList())).thenReturn(List.of(order));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            orderService.importOrders("");
        }
        Mockito.verify(orderDao, Mockito.atLeastOnce()).update(order);
    }

    @Test
    void OrderServiceImpl_importOrders_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(entityCvsConverter.parseOrders(Mockito.anyList())).thenReturn(List.of(order));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            Assertions.assertThrows(BusinessException.class, () -> orderService.importOrders(""));
        }
    }
}
