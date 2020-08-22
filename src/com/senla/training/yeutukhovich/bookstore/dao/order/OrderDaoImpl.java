package com.senla.training.yeutukhovich.bookstore.dao.order;

import com.senla.training.yeutukhovich.bookstore.dao.AbstractEntityDao;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.Fields;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class OrderDaoImpl extends AbstractEntityDao<Order> implements OrderDao {

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
    public List<Order> findCompletedOrdersBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_COMPLETED_ORDERS_BETWEEN_DATES)) {
            List<Order> orders = new ArrayList<>();
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(parseResultSetEntity(resultSet));
            }
            return orders;
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    protected String getFindAllQuery() {
        return FIND_ALL;
    }

    @Override
    protected String getFindByIdQuery() {
        return FIND_BY_ID;
    }

    @Override
    protected String getInsertQuery() {
        return ADD_ORDER;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_ORDER;
    }

    @Override
    protected Order parseResultSetEntity(ResultSet resultSet) throws ParseException, SQLException {
        if (resultSet == null) {
            return null;
        }
        Order order = new Order();
        order.setId(resultSet.getLong(Fields.ORDER_ID.getFieldName()));
        order.setState(OrderState.valueOf(resultSet.getString(Fields.STATE_TYPE.getFieldName())));
        order.setCurrentBookPrice(resultSet.getBigDecimal(Fields.CURRENT_PRICE.getFieldName()));
        order.setCreationDate(new Date(resultSet.getTimestamp(Fields.CREATION_DATE.getFieldName()).getTime()));
        Optional.ofNullable(resultSet.getTimestamp(Fields.COMPLETION_DATE.getFieldName()))
                .ifPresent(timestamp -> order.setCompletionDate(new Date(timestamp.getTime())));
        order.setCustomerData(resultSet.getString(Fields.CUSTOMER_DATA.getFieldName()));
        Book book = new Book();
        book.setId(resultSet.getLong(Fields.BOOK_ID.getFieldName()));
        book.setTitle(resultSet.getString(Fields.TITLE.getFieldName()));
        book.setAvailable(resultSet.getBoolean(Fields.IS_AVAILABLE.getFieldName()));
        book.setEditionDate(DateConverter.parseDate(String.valueOf(resultSet.getInt(Fields.EDITION_DATE.getFieldName())),
                DateConverter.YEAR_DATE_FORMAT));
        book.setReplenishmentDate(new Date(resultSet.getTimestamp(Fields.REPLENISHMENT_DATE.getFieldName()).getTime()));
        book.setPrice(resultSet.getBigDecimal(Fields.PRICE.getFieldName()));
        order.setBook(book);
        return order;
    }

    @Override
    protected void prepareInsert(PreparedStatement preparedStatement, Order entity) throws SQLException {
        preparedStatement.setLong(1, entity.getBook().getId());
        preparedStatement.setInt(2, OrderState.CREATED.getIdNumber());
        preparedStatement.setBigDecimal(3, entity.getCurrentBookPrice());
        preparedStatement.setTimestamp(4, new Timestamp(entity.getCreationDate().getTime()));
        preparedStatement.setString(5, entity.getCustomerData());
    }

    @Override
    protected void prepareUpdate(PreparedStatement preparedStatement, Order entity) throws SQLException {
        preparedStatement.setLong(1, entity.getBook().getId());
        preparedStatement.setInt(2, entity.getState().getIdNumber());
        preparedStatement.setBigDecimal(3, entity.getCurrentBookPrice());
        preparedStatement.setTimestamp(4, new Timestamp(entity.getCreationDate().getTime()));
        preparedStatement.setTimestamp(5,
                entity.getCompletionDate() == null ? null : new Timestamp(entity.getCompletionDate().getTime()));
        preparedStatement.setString(6, entity.getCustomerData());
        preparedStatement.setLong(7, entity.getId());
    }
}
