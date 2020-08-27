package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;

import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityDao<T extends AbstractEntity> implements GenericDao<T> {

    protected static final String ORDER_BY = "ORDER BY";
    protected static final String DESC = "DESC";

    protected abstract String getFindAllQuery();

    protected abstract String getFindByIdQuery();

    protected abstract String getInsertQuery();

    protected abstract String getUpdateQuery();

    protected abstract T parseResultSetEntity(ResultSet resultSet) throws ParseException, SQLException;

    protected abstract void prepareInsert(PreparedStatement preparedStatement, T entity) throws SQLException;

    protected abstract void prepareUpdate(PreparedStatement preparedStatement, T entity) throws SQLException;

    @Override
    public List<T> findAll(Connection connection) {
        return findAll(connection, "");
    }

    @Override
    public List<T> findAll(Connection connection, String sortingQueryPart) {
        List<T> entities = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(getFindAllQuery() + sortingQueryPart);
            while (resultSet.next()) {
                entities.add(parseResultSetEntity(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return entities;
    }

    @Override
    public Optional<T> findById(Connection connection, Long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getFindByIdQuery())) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(parseResultSetEntity(resultSet));
            }
        } catch (SQLException | ParseException e) {
            throw new InternalException(e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public Long add(Connection connection, T entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getInsertQuery(),
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            prepareInsert(preparedStatement, entity);
            preparedStatement.execute();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Connection connection, T entity) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateQuery())) {
            prepareUpdate(preparedStatement, entity);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
