package com.senla.training.yeutukhovich.bookstore.model.service.dto.mapper;

import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto map(Book book) {
        if (book == null) {
            return null;
        }
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAvailable(book.isAvailable());
        bookDto.setEditionYear(book.getEditionYear());
        bookDto.setReplenishmentDate(book.getReplenishmentDate());
        bookDto.setPrice(book.getPrice());
        return bookDto;
    }

    public Book map(BookDto bookDto) {
        if (bookDto == null) {
            return null;
        }
        Book book = new Book();
        book.setId(bookDto.getId());
        book.setTitle(bookDto.getTitle());
        book.setAvailable(bookDto.getAvailable());
        book.setEditionYear(bookDto.getEditionYear());
        book.setReplenishmentDate(bookDto.getReplenishmentDate());
        book.setPrice(bookDto.getPrice());
        return book;
    }
}
