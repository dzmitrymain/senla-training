package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.converter.DBResultSetParser;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookDaoImpl implements BookDao {

    private static final String FIND_ALL = "SELECT id AS book_id, title, is_available, edition_date, replenishment_date, price FROM books;";
    private static final String FIND_BY_ID = "SELECT id AS book_id, title, is_available, edition_date, replenishment_date, price FROM books WHERE id=?;";
    private static final String ADD_BOOK = "INSERT INTO books (title, is_available, edition_date, price) VALUES (?,?,?,?);";
    private static final String UPDATE_BOOK_BY_ID = "UPDATE books SET title=?, is_available=?, edition_date=?, price=? WHERE id=?;";

    @Override
    public List<Book> findAll(Connection connection) {
        List<Book> books = new ArrayList<>();
        if (connection == null) {
            return books;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            while (resultSet.next()) {
                books.add(DBResultSetParser.parseBook(resultSet));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Connection connection, Book entity) {
        if (connection == null || entity == null) {
            return;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setBoolean(2, entity.isAvailable());
            preparedStatement.setInt(3, Integer.parseInt(DateConverter.formatDate(entity.getEditionDate(), DateConverter.YEAR_DATE_FORMAT)));
            preparedStatement.setBigDecimal(4, entity.getPrice());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Connection connection, Book entity) {
        if (connection == null || entity == null) {
            return;
        }
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_BOOK_BY_ID)) {
            preparedStatement.setString(1, entity.getTitle());
            preparedStatement.setBoolean(2, entity.isAvailable());
            preparedStatement.setInt(3, Integer.parseInt(DateConverter.formatDate(entity.getEditionDate(), DateConverter.YEAR_DATE_FORMAT)));
            preparedStatement.setBigDecimal(4, entity.getPrice());
            preparedStatement.setLong(5, entity.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
