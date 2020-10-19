package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public BookDto updateBook(@PathVariable("id") Long id, @RequestBody BookDto bookDto) {
        return bookService.updateBook(id, bookDto);
    }

    @GetMapping()
    public List<BookDto> findSortedBooks(@RequestParam("sort") String sortParam) {
        return bookService.findSortedAllBooks(sortParam);
    }

    @GetMapping("/soldBetweenDates")
    public List<BookDto> findSoldBooksBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return bookService.findSoldBooksBetweenDates(startDate, endDate);
    }

    @GetMapping("/unsoldBetweenDates")
    public List<BookDto> findUnsoldBooksBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return bookService.findUnsoldBooksBetweenDates(startDate, endDate);
    }

    @GetMapping("/stale")
    public List<BookDto> findStaleBooks() {
        return bookService.findStaleBooks();
    }

    @GetMapping("/{id}/description")
    public BookDescriptionDto showBookDescription(@PathVariable("id") Long id) {
        return bookService.showBookDescription(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/import")
    public List<BookDto> importBooks(@RequestParam String fileName) {
        return bookService.importBooks(fileName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/export")
    public List<BookDto> exportAllBooks(@RequestParam String fileName) {
        return bookService.exportAllBooks(fileName);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/export")
    public BookDto exportBook(@PathVariable("id") Long bookId, @RequestParam String fileName) {
        return bookService.exportBook(bookId, fileName);
    }
}
