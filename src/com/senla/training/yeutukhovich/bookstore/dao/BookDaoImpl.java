package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.converter.DBResultSetParser;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookDaoImpl implements BookDao {

    private static final String FIND_ALL = "SELECT id AS book_id, title, is_available, edition_date, replenishment_date, price FROM books;";
    private static final String FIND_BY_ID = "SELECT id AS book_id, title, is_available, edition_date, replenishment_date, price FROM books WHERE id=?;";
    private static final String ADD_BOOK = "INSERT INTO books (title, is_available, edition_date, price) VALUES (?,?,?,?);";
    private static final String UPDATE_BOOK_BY_ID = "UPDATE books SET title=?, is_available=?, edition_date=?, price=? WHERE id=?;";
    private static final String FIND_SOLD_BOOKS_BETWEEN_DATES = "SELECT DISTINCT books.id AS book_id, title, is_available," +
            " edition_date, replenishment_date, books.price FROM orders JOIN books ON books.id=orders.book_id WHERE " +
            "(completion_date BETWEEN ? AND ?) AND order_states_id=3;";
    private static final String FIND_UNSOLD_BOOKS_BETWEEN_DATES = "SELECT books.id AS book_id, title, is_available, " +
            "edition_date, replenishment_date, books.price FROM books WHERE books.id NOT IN (SELECT DISTINCT books.id FROM " +
            "orders JOIN books ON books.id=orders.book_id WHERE (completion_date BETWEEN ? AND ?) AND order_states_id=3);";
    private static final String FIND_STALE_BOOKS_BETWEEN_DATES = "SELECT books.id AS book_id, title, is_available, " +
            "edition_date, replenishment_date, books.price FROM books WHERE replenishment_date <= ?" +
            " AND books.id NOT IN (SELECT DISTINCT books.id FROM orders JOIN books ON books.id=orders.book_id WHERE " +
            "(completion_date BETWEEN ? AND ?) AND order_states_id=3);";


    @Override
    public List<Book> findAll(Connection connection) {
        List<Book> books = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            while (resultSet.next()) {
                books.add(DBResultSetParser.parseBook(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Connection connection, Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(DBResultSetParser.parseBook(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Long add(Connection connection, Book entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK,
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setBoolean(2, entity.isAvailable());
            preparedStatement.setInt(3, Integer.parseInt(DateConverter.formatDate(entity.getEditionDate(),
                    DateConverter.YEAR_DATE_FORMAT)));
            preparedStatement.setBigDecimal(4, entity.getPrice());
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
    public void update(Connection connection, Book entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_BY_ID)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setBoolean(2, entity.isAvailable());
            preparedStatement.setInt(3, Integer.parseInt(DateConverter.formatDate(entity.getEditionDate(), DateConverter.YEAR_DATE_FORMAT)));
            preparedStatement.setBigDecimal(4, entity.getPrice());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Connection connection, Date startDate, Date endDate) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_SOLD_BOOKS_BETWEEN_DATES)) {
            List<Book> books = new ArrayList<>();
            preparedStatement.setTimestamp(1, new Timestamp(startDate.getTime()));
            preparedStatement.setTimestamp(2, new Timestamp(endDate.getTime()));
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                books.add(DBResultSetParser.parseBook(resultSet));
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
                books.add(DBResultSetParser.parseBook(resultSet));
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
                books.add(DBResultSetParser.parseBook(resultSet));
            }
            return books;
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
