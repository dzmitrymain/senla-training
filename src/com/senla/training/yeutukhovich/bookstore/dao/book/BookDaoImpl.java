package com.senla.training.yeutukhovich.bookstore.dao.book;

import com.senla.training.yeutukhovich.bookstore.dao.AbstractEntityDao;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.Fields;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Singleton
public class BookDaoImpl extends AbstractEntityDao<Book> implements BookDao {

    private static final String FIND_ALL = "SELECT id AS book_id, title, is_available, edition_year, replenishment_date," +
            " price FROM books;";
    private static final String FIND_BY_ID = "SELECT id AS book_id, title, is_available, edition_year, replenishment_date," +
            " price FROM books WHERE id=?;";
    private static final String ADD_BOOK = "INSERT INTO books (title, is_available, edition_year, price) VALUES (?,?,?,?);";
    private static final String UPDATE_BOOK_BY_ID = "UPDATE books SET title=?, is_available=?, edition_year=?, price=? WHERE id=?;";
    private static final String FIND_SOLD_BOOKS_BETWEEN_DATES = "SELECT DISTINCT books.id AS book_id, title, is_available," +
            " edition_year, replenishment_date, books.price FROM orders JOIN books ON books.id=orders.book_id WHERE " +
            "(completion_date BETWEEN ? AND ?) AND order_states_id=3;";
    private static final String FIND_UNSOLD_BOOKS_BETWEEN_DATES = "SELECT books.id AS book_id, title, is_available, " +
            "edition_year, replenishment_date, books.price FROM books WHERE books.id NOT IN (SELECT DISTINCT books.id FROM " +
            "orders JOIN books ON books.id=orders.book_id WHERE (completion_date BETWEEN ? AND ?) AND order_states_id=3);";
    private static final String FIND_STALE_BOOKS_BETWEEN_DATES = "SELECT books.id AS book_id, title, is_available, " +
            "edition_year, replenishment_date, books.price FROM books WHERE replenishment_date <= ?" +
            " AND books.id NOT IN (SELECT DISTINCT books.id FROM orders JOIN books ON books.id=orders.book_id WHERE " +
            "(completion_date BETWEEN ? AND ?) AND order_states_id=3);";


    @Override
    public List<Book> findSoldBooksBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_SOLD_BOOKS_BETWEEN_DATES)) {
            List<Book> books = new ArrayList<>();
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(parseResultSetEntity(resultSet));
            }
            return books;
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_UNSOLD_BOOKS_BETWEEN_DATES)) {
            List<Book> books = new ArrayList<>();
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(parseResultSetEntity(resultSet));
            }
            return books;
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findStaleBooksBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_STALE_BOOKS_BETWEEN_DATES)) {
            List<Book> books = new ArrayList<>();
            Timestamp startTimestamp = new Timestamp(startDate.getTime());
            preparedStatement.setTimestamp(1, startTimestamp);
            preparedStatement.setTimestamp(2, startTimestamp);
            preparedStatement.setTimestamp(3, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(parseResultSetEntity(resultSet));
            }
            return books;
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
        return ADD_BOOK;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_BOOK_BY_ID;
    }

    @Override
    protected Book parseResultSetEntity(ResultSet resultSet) throws ParseException, SQLException {
        if (resultSet == null) {
            return null;
        }
        Book book = new Book();
        book.setId(resultSet.getLong(Fields.BOOK_ID.getFieldName()));
        book.setTitle(resultSet.getString(Fields.TITLE.getFieldName()));
        book.setAvailable(resultSet.getBoolean(Fields.IS_AVAILABLE.getFieldName()));
        book.setEditionYear(resultSet.getInt(Fields.EDITION_YEAR.getFieldName()));
        book.setReplenishmentDate(new Date(resultSet.getTimestamp(Fields.REPLENISHMENT_DATE.getFieldName()).getTime()));
        book.setPrice(resultSet.getBigDecimal(Fields.PRICE.getFieldName()));
        return book;
    }

    @Override
    protected void prepareInsert(PreparedStatement preparedStatement, Book entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setBoolean(2, entity.isAvailable());
        preparedStatement.setInt(3, entity.getEditionYear());
        preparedStatement.setBigDecimal(4, entity.getPrice());
    }

    @Override
    protected void prepareUpdate(PreparedStatement preparedStatement, Book entity) throws SQLException {
        preparedStatement.setString(1, entity.getTitle());
        preparedStatement.setBoolean(2, entity.isAvailable());
        preparedStatement.setInt(3, entity.getEditionYear());
        preparedStatement.setBigDecimal(4, entity.getPrice());
        preparedStatement.setLong(5, entity.getId());
    }
}
