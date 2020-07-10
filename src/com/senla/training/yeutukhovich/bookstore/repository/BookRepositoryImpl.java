package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookRepositoryImpl implements BookRepository {

    @Autowired
    private IdGenerator idGenerator;
    private List<Book> books = new ArrayList<>();

    private BookRepositoryImpl() {

    }

    @Override
    public List<Book> findAll() {
        return List.copyOf(books);
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst();
    }

    @Override
    public void add(Book entity) {
        if (entity != null && !books.contains(entity)) {
            entity.setId(idGenerator.getNextBookIdNumber());
            books.add(entity);
        }
    }

    @Override
    public void update(Book entity) {
        if (entity != null) {
            int index = books.indexOf(entity);
            if (index != -1) {
                books.set(index, entity);
            }
        }
    }
}


