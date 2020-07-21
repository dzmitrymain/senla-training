package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository {

    List<Request> findAll();

    Optional<Request> findById(Long id);

    void add(Request entity);

    void update(Request entity);
}
