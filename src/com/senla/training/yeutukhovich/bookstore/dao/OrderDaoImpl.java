package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.util.converter.DBResultSetParser;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
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

    @Override
    public List<Order> findAll(Connection connection) {
        List<Order> orders = new ArrayList<>();
        if (connection == null) {
            return orders;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            while (resultSet.next()) {
                orders.add(DBResultSetParser.parseOrder(resultSet));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
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
        } catch (SQLException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Connection connection, Order entity) {
        if (connection == null || entity == null) {
            return;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_ORDER)) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setInt(2, OrderState.CREATED.getIdNumber());
            preparedStatement.setBigDecimal(3, entity.getCurrentBookPrice());
            preparedStatement.setTimestamp(4, new Timestamp(entity.getCreationDate().getTime()));
            preparedStatement.setString(5, entity.getCustomerData());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //TODO: If cancel, set completion_date
    @Override
    public void update(Connection connection, Order entity) {
        if (connection == null || entity == null) {
            return;
        }
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
            e.printStackTrace();
        }
    }
}
