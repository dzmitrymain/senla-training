package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.stream.Collectors;

@Singleton
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private static final String ORDER_DELIMITER = "\n";

    @Autowired
    private OrderService orderService;

    public String createOrder(Long bookId, String customerData) {
        try {
            CreationOrderResult creationOrderResult = orderService.createOrder(bookId, customerData);
            LOGGER.info(LoggerConstant.CREATE_ORDER_SUCCESS.getMessage(), creationOrderResult.getOrderId(), bookId);
            if (creationOrderResult.getRequestId() == null) {
                return MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage();
            }
            return MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage() + "\n"
                    + MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.CREATE_ORDER_FAIL.getMessage(), bookId);
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String cancelOrder(Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            LOGGER.info(LoggerConstant.CANCEL_ORDER_SUCCESS.getMessage(), orderId);
            return MessageConstant.ORDER_HAS_BEEN_CANCELED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.CANCEL_ORDER_FAIL.getMessage(), orderId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String completeOrder(Long orderId) {
        try {
            orderService.completeOrder(orderId);
            LOGGER.info(LoggerConstant.COMPLETE_ORDER_SUCCESS.getMessage(), orderId);
            return MessageConstant.ORDER_HAS_BEEN_COMPLETED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.COMPLETE_ORDER_FAIL.getMessage(), orderId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByCompletionDate() {
        try {
            String result = orderService.findSortedAllOrdersByCompletionDate().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_COMPLETION_DATE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByPrice() {
        try {
            String result = orderService.findSortedAllOrdersByPrice().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_PRICE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByState() {
        try {
            String result = orderService.findSortedAllOrdersByState().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_ALL_ORDERS_SORTED_BY_STATE.getMessage());
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        try {
            String result = orderService.findCompletedOrdersBetweenDates(startDate, endDate).stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
            LOGGER.info(LoggerConstant.FIND_COMPLETED_ORDERS_BETWEEN_DATES.getMessage(),
                    DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                    DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String calculateProfitBetweenDates(Date startDate, Date endDate) {
        try {
            String result = orderService.calculateProfitBetweenDates(startDate, endDate).toString();
            LOGGER.info(LoggerConstant.SHOW_PROFIT_BETWEEN_DATES.getMessage(),
                    DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                    DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        try {
            String result = String.valueOf(orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate));
            LOGGER.info(LoggerConstant.SHOW_COMPLETE_ORDERS_NUMBER_BETWEEN_DATES.getMessage(),
                    DateConverter.formatDate(startDate, DateConverter.DAY_DATE_FORMAT),
                    DateConverter.formatDate(endDate, DateConverter.DAY_DATE_FORMAT));
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String showOrderDetails(Long orderId) {
        try {
            String result = orderService.showOrderDetails(orderId).toString();
            LOGGER.info(LoggerConstant.SHOW_ORDER_DETAILS_SUCCESS.getMessage(), orderId);
            return result;
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.SHOW_ORDER_DETAILS_FAIL.getMessage(), orderId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importOrders(String fileName) {
        try {
            String result = MessageConstant.IMPORTED_ENTITIES.getMessage() + orderService.importOrders(fileName);
            LOGGER.info(LoggerConstant.IMPORT_ORDERS_SUCCESS.getMessage(), fileName);
            return result;
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.IMPORT_ORDERS_FAIL.getMessage(), e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllOrders(String fileName) {
        try {
            String result = MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + orderService.exportAllOrders(fileName);
            LOGGER.info(LoggerConstant.EXPORT_ALL_ORDERS.getMessage(), fileName);
            return result;
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportOrder(Long orderId, String fileName) {
        try {
            orderService.exportOrder(orderId, fileName);
            LOGGER.info(LoggerConstant.EXPORT_ORDER_SUCCESS.getMessage(), orderId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            LOGGER.warn(LoggerConstant.EXPORT_ORDER_FAIL.getMessage(), orderId, e.getMessage());
            return e.getMessage();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
