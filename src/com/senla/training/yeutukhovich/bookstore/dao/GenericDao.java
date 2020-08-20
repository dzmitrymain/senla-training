package com.senla.training.yeutukhovich.bookstore.dao;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends AbstractEntity> {

    List<T> findAll(Connection connection);

    Optional<T> findById(Connection connection, Long id);

    Long add(Connection connection, T entity);

    void update(Connection connection, T entity);
}
