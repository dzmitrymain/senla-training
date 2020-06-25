package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookRepository implements IBookRepository {

    private static IBookRepository instance;

    private List<Book> books = new ArrayList<>();

    private BookRepository() {

    }

    public static IBookRepository getInstance() {
        if (instance == null) {
            instance = new BookRepository();
            BooksInitializer.initBooks();
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

    private static class BooksInitializer {
        private static void initBooks() {
            instance.add(new Book("Jonathan Livingston Seagull", true,
                    DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT),
                    new Date(), BigDecimal.valueOf(25.90)));
            instance.add(new Book("Hard to be a god", true,
                    DateConverter.parseDate("1964", DateConverter.YEAR_DATE_FORMAT),
                    new Date(), BigDecimal.valueOf(29.99)));
            instance.add(new Book("Hotel \"At a Lost Climber\"",
                    false, DateConverter.parseDate("1970", DateConverter.YEAR_DATE_FORMAT),
                    null, BigDecimal.valueOf(22.50)));
            instance.add(new Book("Roadside Picnic", false,
                    DateConverter.parseDate("1972", DateConverter.YEAR_DATE_FORMAT),
                    null, BigDecimal.valueOf(19.95)));
            instance.add(new Book("Prisoners of Power",
                    true, DateConverter.parseDate("1971", DateConverter.YEAR_DATE_FORMAT),
                    new Date(), BigDecimal.valueOf(35.90)));
        }
    }
}


