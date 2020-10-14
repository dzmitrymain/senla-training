package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;

import java.util.Date;
import java.util.List;

public interface BookService {

    BookDto updateBook(Long id, BookDto bookDto);

    List<BookDto> findSortedAllBooks(String sortParam);

    List<BookDto> findSoldBooksBetweenDates(Date startDate, Date endDate);

    List<BookDto> findUnsoldBooksBetweenDates(Date startDate, Date endDate);

    List<BookDto> findStaleBooks();

    BookDescriptionDto showBookDescription(Long id);

    List<BookDto> exportAllBooks(String fileName);

    BookDto exportBook(Long bookId, String fileName);

    List<BookDto> importBooks(String fileName);
}
