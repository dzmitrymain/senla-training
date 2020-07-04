package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// нужно придти к какому-то единому наименованию
// или IRepository - Repository
// или Repository - RepositoryImpl
// во всем проекте
public class BookRepository implements IBookRepository {

    private static IBookRepository instance;

    private List<Book> books = new ArrayList<>();

    private BookRepository() {

    }

    public static IBookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
        }
        return instance;
    }

    @Override
    public List<Book> findAll() {
        return List.copyOf(books);
    }

    @Override
    public Book findById(Long id) {
        // тренируй стримы и опшнал
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
            entity.setId(IdGenerator.getInstance().getNextBookIdNumber());
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


