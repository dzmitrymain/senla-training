package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.List;

public interface IRequestRepository {

    List<Request> findAll();

    Request findById(Long id);

    void add(Request entity);

    void update(Request entity);
}
