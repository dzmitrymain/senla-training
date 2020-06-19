package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.util.List;

public interface IRepository<T extends AbstractEntity> {

    List<T> findAll();

    T findById(Long id);

    void add(T entity);

    void update(T entity);
}
