package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;

import java.util.List;

public class BookRepository implements IRepository<Book> {

    private static IRepository<Book> instance;

    private List<Book> books;

    private BookRepository(List<Book> books) {
        this.books = books;
    }

    public static IRepository<Book> getInstance() {
        if (instance == null) {
            instance = new BookRepository(EntityInitializer.getBooks());
        }
        return instance;
    }

    @Override
    public List<Book> findAll() {
        return List.copyOf(books);
    }

    @Override
    public Book findById(Long id) {
        if (id != null) {
            for (Book book : books) {
                if (book.getId().equals(id)) {
                    return book;
                }
            }
        }
        return null;
    }

    @Override
    public void add(Book entity) {
        if (entity != null && !books.contains(entity)) {
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
