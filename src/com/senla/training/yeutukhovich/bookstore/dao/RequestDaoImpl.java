package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.converter.DBResultSetParser;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class RequestDaoImpl implements RequestDao {

    private static final String FIND_ALL = "SELECT requests.id AS request_id, book_id, is_active, requester_data, title," +
            "is_available, edition_date, replenishment_date, price FROM requests JOIN books ON requests.book_id=books.id;";
    private static final String FIND_BY_ID = "SELECT requests.id AS request_id, book_id, is_active, requester_data, title, " +
            "is_available, edition_date, replenishment_date, price FROM requests JOIN books ON requests.book_id=books.id " +
            "WHERE requests.id=?";
    private static final String ADD_REQUEST = "INSERT INTO requests (book_id, is_active, requester_data) VALUES (?,?,?);";
    private static final String UPDATE_REQUEST = "UPDATE requests SET book_id=?, is_active=?, requester_data=? " +
            "WHERE id=?;";

    @Override
    public List<Request> findAll(Connection connection) {
        List<Request> requests = new ArrayList<>();
        if (connection == null) {
            return requests;
        }
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(FIND_ALL);
            while (resultSet.next()) {
                requests.add(DBResultSetParser.parseRequest(resultSet));
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public Optional<Request> findById(Connection connection, Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(DBResultSetParser.parseRequest(resultSet));
            }
        } catch (SQLException | ParseException | NullPointerException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void add(Connection connection, Request entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(ADD_REQUEST)) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setBoolean(2,entity.isActive());
            preparedStatement.setString(3, entity.getRequesterData());

            preparedStatement.execute();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Connection connection, Request entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_REQUEST)) {
            preparedStatement.setLong(1, entity.getBook().getId());
            preparedStatement.setBoolean(2, entity.isActive());
            preparedStatement.setString(3, entity.getRequesterData());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.execute();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }
}
