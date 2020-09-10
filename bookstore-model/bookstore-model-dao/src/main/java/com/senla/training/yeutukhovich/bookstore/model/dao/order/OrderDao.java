package com.senla.training.yeutukhovich.bookstore.model.dao.order;

import com.senla.training.yeutukhovich.bookstore.model.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderDao extends GenericDao<Order, Long> {

    List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate);

    BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate);

    Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate);

    List<Order> findSortedAllOrdersByCompletionDate();

    List<Order> findSortedAllOrdersByPrice();

    List<Order> findSortedAllOrdersByState();
}
