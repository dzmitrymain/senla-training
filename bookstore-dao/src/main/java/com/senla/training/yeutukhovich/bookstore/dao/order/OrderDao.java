package com.senla.training.yeutukhovich.bookstore.dao.order;

import com.senla.training.yeutukhovich.bookstore.dao.GenericDao;
import com.senla.training.yeutukhovich.bookstore.domain.Order;

import java.sql.Connection;
import java.util.Date;
import java.util.List;

public interface OrderDao extends GenericDao<Order> {

    List<Order> findCompletedOrdersBetweenDates(Connection connection, Date startDate, Date endDate);

    List<Order> findSortedAllOrdersByCompletionDate(Connection connection);

    List<Order> findSortedAllOrdersByPrice(Connection connection);

    List<Order> findSortedAllOrdersByState(Connection connection);
}
