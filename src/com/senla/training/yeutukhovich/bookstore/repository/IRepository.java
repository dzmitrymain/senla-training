package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.util.List;

// неплохо, но ровно до тех пор, пока репозитории содержат только круд методы
// когда понадобится что-то специфичное для конкретной модели,
// надо будет написать свой интерфейс, который расширит этот интерфейс
// мы будем делать это, когда перейдем к работе с БД
public interface IRepository<T extends AbstractEntity> {

    List<T> findAll();

    T findById(Long id);

    void add(T entity);

    void update(T entity);
}
