package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.Date;
import java.util.stream.Collectors;

@Singleton
public class OrderController {

    private static final String ORDER_DELIMITER = "\n";

    @Autowired
    private OrderService orderService;

    public String createOrder(Long bookId, String customerData) {
        try {
            CreationOrderResult creationOrderResult = orderService.createOrder(bookId, customerData);
            if (creationOrderResult.getRequestId() == null) {
                return MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage();
            }
            return MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage() + "\n"
                    + MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String cancelOrder(Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return MessageConstant.ORDER_HAS_BEEN_CANCELED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String completeOrder(Long orderId) {
        try {
            orderService.completeOrder(orderId);
            return MessageConstant.ORDER_HAS_BEEN_COMPLETED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByCompletionDate() {
        try {
            return orderService.findSortedAllOrdersByCompletionDate().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByPrice() {
        try {
            return orderService.findSortedAllOrdersByPrice().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findSortedAllOrdersByState() {
        try {
            return orderService.findSortedAllOrdersByState().stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        try {
            return orderService.findCompletedOrdersBetweenDates(startDate, endDate).stream()
                    .map(Order::toString)
                    .collect(Collectors.joining(ORDER_DELIMITER));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String calculateProfitBetweenDates(Date startDate, Date endDate) {
        try {
            return orderService.calculateProfitBetweenDates(startDate, endDate).toString();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        try {
            return String.valueOf(orderService.calculateCompletedOrdersNumberBetweenDates(startDate, endDate));
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String showOrderDetails(Long orderId) {
        try {
            return orderService.showOrderDetails(orderId).toString();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String importOrders(String fileName) {
        try {
            return MessageConstant.IMPORTED_ENTITIES.getMessage() + orderService.importOrders(fileName);
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }

    public String exportAllOrders(String fileName) {
        try {
            return MessageConstant.EXPORTED_ENTITIES.getMessage()
                    + orderService.exportAllOrders(fileName);
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }

    }

    public String exportOrder(Long orderId, String fileName) {
        try {
            orderService.exportOrder(orderId, fileName);
            return MessageConstant.ENTITY_EXPORTED.getMessage();
        } catch (BusinessException e) {
            return e.getMessage();
        } catch (InternalException e) {
            System.err.println(e.getMessage()); //log
            return MessageConstant.SOMETHING_WENT_WRONG.getMessage();
        }
    }
}
