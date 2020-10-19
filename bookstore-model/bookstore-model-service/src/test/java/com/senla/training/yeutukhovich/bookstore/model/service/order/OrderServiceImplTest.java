package com.senla.training.yeutukhovich.bookstore.model.service.order;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCsvConverter;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
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
    private static final OrderDto orderDto = Mockito.mock(OrderDto.class);
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
    private EntityCsvConverter entityCsvConverter;

    @BeforeAll
    static void setup() {
        Mockito.when(order.getId()).thenReturn(orderId);
        Mockito.when(book.getId()).thenReturn(bookId);
        Mockito.when(order.getBook()).thenReturn(book);
        Mockito.when(orderDto.getId()).thenReturn(orderId);
    }

    @BeforeEach
    void setUp() {
        Mockito.clearInvocations(bookDao, orderDao, requestDao, entityCsvConverter);
    }

    @Test
    void OrderServiceImpl_createOrder_shouldCreateOrderAndRequestIfBookNotAvailable() {
        Mockito.when(book.getAvailable()).thenReturn(false);
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        OrderDto result = orderService.createOrder(bookId, "");

        Mockito.verify(requestDao, Mockito.times(1)).add(Mockito.any());
        Mockito.verify(orderDao, Mockito.times(1)).add(Mockito.any());
        Assertions.assertNotNull(result);
    }

    @Test
    void OrderServiceImpl_createOrder_shouldCreateOrderWithoutRequestIfBookAvailable() {
        Mockito.when(book.getAvailable()).thenReturn(true);
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        OrderDto result = orderService.createOrder(bookId, "");

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
    void OrderServiceImpl_updateOrder_shouldUpdateIfOrderExistAndStateCreated() {
        Mockito.when(orderDto.getState()).thenReturn(OrderState.COMPLETED.toString());
        Mockito.when(orderDto.getId()).thenReturn(orderId);
        Mockito.when(order.getBook().getAvailable()).thenReturn(true);
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(order.getState()).thenReturn(OrderState.CREATED);

        orderService.updateState(orderId, orderDto);

        Mockito.verify(orderDao, Mockito.times(1)).update(order);
    }

    @Test
    void OrderServiceImpl_updateOrder_shouldThrowExceptionIfOrderNotExist() {
        Mockito.when(orderDto.getState()).thenReturn(OrderState.COMPLETED.toString());
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.updateState(orderId, orderDto));

        Assertions.assertEquals(MessageConstant.ORDER_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_updateOrder_shouldThrowExceptionIfStateNotCreated() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(order.getState()).thenReturn(null);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> orderService.updateState(orderId, orderDto));

        Assertions.assertEquals(MessageConstant.WRONG_ORDER_STATE.getMessage(), exception.getMessage());
    }

    @Test
    void OrderServiceImpl_findSortedAllOrders_paramCompletionDate() {
        orderService.findSortedAllOrders("COMPLETION");

        Mockito.verify(orderDao, Mockito.times(1)).findSortedAllOrdersByCompletionDate();
    }

    @Test
    void OrderServiceImpl_findSortedAllOrders_paramPrice() {
        orderService.findSortedAllOrders("PRICE");

        Mockito.verify(orderDao, Mockito.times(1)).findSortedAllOrdersByPrice();
    }

    @Test
    void OrderServiceImpl_findSortedAllOrders_paramState() {
        orderService.findSortedAllOrders("STATE");

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
        Mockito.verify(entityCsvConverter, Mockito.times(1)).convertOrders(Mockito.anyList());
        Mockito.verify(orderDao, Mockito.times(1)).findAll();
    }

    @Test
    void OrderServiceImpl_exportOrder() {
        Mockito.when(orderDao.findById(orderId)).thenReturn(Optional.of(order));

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            orderService.exportOrder(orderId, "");
        }
        Mockito.verify(entityCsvConverter, Mockito.times(1)).convertOrders(Mockito.anyList());
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
        Mockito.when(entityCsvConverter.parseOrders(Mockito.anyList())).thenReturn(List.of(order));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            orderService.importOrders("");
        }
        Mockito.verify(orderDao, Mockito.atLeastOnce()).update(order);
    }

    @Test
    void OrderServiceImpl_importOrders_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(entityCsvConverter.parseOrders(Mockito.anyList())).thenReturn(List.of(order));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            Assertions.assertThrows(BusinessException.class, () -> orderService.importOrders(""));
        }
    }
}
