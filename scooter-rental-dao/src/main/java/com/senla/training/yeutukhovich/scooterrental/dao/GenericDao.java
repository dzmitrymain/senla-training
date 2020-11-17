package com.senla.training.yeutukhovich.scooterrental.dao;

import com.senla.training.yeutukhovich.scooterrental.domain.AbstractEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface GenericDao<T extends AbstractEntity, PK extends Serializable> {

    List<T> findAll();

    Optional<T> findById(PK id);

    PK add(T entity);

    T update(T entity);

    Optional<T> delete(PK id);
}
