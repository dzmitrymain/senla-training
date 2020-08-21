package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.converter.DBResultSetParser;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class OrderDaoImpl implements OrderDao {

    private static final String FIND_ALL = "SELECT orders.id AS order_id, book_id, state_type," +
            " orders.price AS current_price, creation_date, completion_date, customer_data, title, " +
            "is_available, edition_date, replenishment_date, books.price FROM orders " +
            "JOIN books ON books.id=orders.book_id JOIN order_states ON orders.order_states_id=order_states.id;";
    private static final String FIND_BY_ID = "SELECT orders.id AS order_id, book_id, state_type," +
            " orders.price AS current_price, creation_date, completion_date, customer_data, title, " +
            "is_available, edition_date, replenishment_date, books.price FROM orders " +
            "JOIN books ON books.id=orders.book_id JOIN order_states ON orders.order_states_id=order_states.id " +
            "WHERE orders.id=?;";
    private static final String ADD_ORDER = "INSERT INTO orders (book_id, order_states_id, price, creation_date, " +
            "customer_data) VALUES (?,?,?,?,?);";
    private static final String UPDATE_ORDER = "UPDATE orders SET book_id=?, order_states_id=?, price=?, creation_date=?," +
            "completion_date=?, customer_data=? WHERE id=?";
    private static final String FIND_COMPLETED_ORDERS_BETWEEN_DATES = "SELECT orders.id AS order_id, book_id, state_type, " +
            "orders.price AS current_price, creation_date, completion_date, customer_data, title, " +
            "is_available, edition_date, replenishment_date, books.price FROM orders " +
            "JOIN books ON books.id=orders.book_id JOIN order_states ON orders.order_states_id=order_states.id " +
            "WHERE order_states_id=3 AND (completion_date BETWEEN ? AND ?);";

    @Override
    public List<Order> findAll(Connection connection) {
        List<Order> orders = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            while (resultSet.next()) {
                orders.add(DBResultSetParser.parseOrder(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return orders;
    }

    @Override
    public Optional<Order> findById(Connection connection, Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(DBResultSetParser.parseOrder(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Long add(Connection connection, Order entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_ORDER,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setInt(2, OrderState.CREATED.getIdNumber());
            preparedStatement.setBigDecimal(3, entity.getCurrentBookPrice());
            preparedStatement.setTimestamp(4, new Timestamp(entity.getCreationDate().getTime()));
            preparedStatement.setString(5, entity.getCustomerData());
            preparedStatement.execute();
            ResultSet generetedKeys = preparedStatement.getGeneratedKeys();
            if (generetedKeys.next()) {
                return generetedKeys.getLong(1);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Connection connection, Order entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER)) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setInt(2, entity.getState().getIdNumber());
            preparedStatement.setBigDecimal(3, entity.getCurrentBookPrice());
            preparedStatement.setTimestamp(4, new Timestamp(entity.getCreationDate().getTime()));
            preparedStatement.setTimestamp(5,
                    entity.getCompletionDate() == null ? null : new Timestamp(entity.getCompletionDate().getTime()));
            preparedStatement.setString(6, entity.getCustomerData());
            preparedStatement.setLong(7, entity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_COMPLETED_ORDERS_BETWEEN_DATES)) {
            List<Order> orders = new ArrayList<>();
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(DBResultSetParser.parseOrder(resultSet));
            }
            return orders;
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
