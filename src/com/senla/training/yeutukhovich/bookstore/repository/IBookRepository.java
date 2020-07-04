package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Book;

import java.util.List;

public interface IBookRepository {

    List<Book> findAll();

    Book findById(Long id);

    void add(Book entity);

    void update(Book entity);
}
