package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.BookDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @PutMapping("/replenish/{id}")
    public BookDto replenishBook(@PathVariable("id") Long id) {
        return new BookDto(bookService.replenishBook(id));
    }

    @PutMapping("/writeOff/{id}")
    public BookDto writeOffBook(@PathVariable("id") Long id) {
        return new BookDto(bookService.writeOffBook(id));
    }

    @GetMapping("/allBooksByAvailability")
    public List<BookDto> findSortedAllBooksByAvailability() {
        return bookService.findSortedAllBooksByAvailability().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allBooksByEditionYear")
    public List<BookDto> findSortedAllBooksByEditionYear() {
        return bookService.findSortedAllBooksByEditionYear().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allBooksByPrice")
    public List<BookDto> findSortedAllBooksByPrice() {
        return bookService.findSortedAllBooksByPrice().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allBooksByReplenishmentDate")
    public List<BookDto> findSortedAllBooksByReplenishmentDate() {
        return bookService.findSortedAllBooksByReplenishmentDate().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/allBooksByTitle")
    public List<BookDto> findSortedAllBooksByTitle() {
        return bookService.findSortedAllBooksByTitle().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/soldBetweenDates")
    public List<BookDto> findSoldBooksBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return bookService.findSoldBooksBetweenDates(startDate, endDate).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/unsoldBetweenDates")
    public List<BookDto> findUnsoldBooksBetweenDates(@RequestParam Date startDate, @RequestParam Date endDate) {
        return bookService.findUnsoldBooksBetweenDates(startDate, endDate).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/stale")
    public List<BookDto> findStaleBooks() {
        return bookService.findStaleBooks().stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/description/{id}")
    public BookDescription showBookDescription(@PathVariable("id") Long id) {
        return bookService.showBookDescription(id);
    }

    @PostMapping("/import/{fileName}")
    public List<BookDto> importBooks(@PathVariable("fileName") String fileName) {
        return bookService.importBooks(fileName).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/exportAll/{fileName}")
    public List<BookDto> exportAllBooks(@PathVariable("fileName") String fileName) {
        return bookService.exportAllBooks(fileName).stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/export/{fileName}&{id}")
    public BookDto exportBook(@PathVariable("id") Long bookId, @PathVariable("fileName") String fileName) {
        return new BookDto(bookService.exportBook(bookId, fileName));
    }
}
