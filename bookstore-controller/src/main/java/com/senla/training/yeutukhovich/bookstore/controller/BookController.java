package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/replenish/{id}")
    public BookDto replenishBook(@PathVariable("id") Long id) {
        return bookService.replenishBook(id);
    }

    @PostMapping("/writeOff/{id}")
    public BookDto writeOffBook(@PathVariable("id") Long id) {
        return bookService.writeOffBook(id);
    }

    @GetMapping("/allBooksByAvailability")
    public List<BookDto> findSortedAllBooksByAvailability() {
        return bookService.findSortedAllBooksByAvailability();
    }

    @GetMapping("/allBooksByEditionYear")
    public List<BookDto> findSortedAllBooksByEditionYear() {
        return bookService.findSortedAllBooksByEditionYear();
    }

    @GetMapping("/allBooksByPrice")
    public List<BookDto> findSortedAllBooksByPrice() {
        return bookService.findSortedAllBooksByPrice();
    }

    @GetMapping("/allBooksByReplenishmentDate")
    public List<BookDto> findSortedAllBooksByReplenishmentDate() {
        return bookService.findSortedAllBooksByReplenishmentDate();
    }

    @GetMapping("/allBooksByTitle")
    public List<BookDto> findSortedAllBooksByTitle() {
        return bookService.findSortedAllBooksByTitle();
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

    @GetMapping("/description/{id}")
    public BookDescriptionDto showBookDescription(@PathVariable("id") Long id) {
        return bookService.showBookDescription(id);
    }

    @PostMapping("/import")
    public List<BookDto> importBooks(@RequestParam String fileName) {
        return bookService.importBooks(fileName);
    }

    @GetMapping("/exportAll")
    public List<BookDto> exportAllBooks(@RequestParam String fileName) {
        return bookService.exportAllBooks(fileName);
    }

    @GetMapping("/export/{id}")
    public BookDto exportBook(@PathVariable("id") Long bookId, @RequestParam String fileName) {
        return bookService.exportBook(bookId, fileName);
    }
}
