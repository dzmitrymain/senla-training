package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.PathConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.*;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private static BookService instance;
    private static final int STALE_MONTH_NUMBER = 6;

    private IRepository<Book> bookRepository;
    private IRepository<Order> orderRepository;
    private IRepository<Request> requestRepository;

    private BookServiceImpl() {
        this.bookRepository = BookRepository.getInstance();
        this.orderRepository = OrderRepository.getInstance();
        this.requestRepository = RequestRepository.getInstance();
    }

    public static BookService getInstance() {
        if (instance == null) {
            instance = new BookServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean replenishBook(Long id) {
        Book checkedBook = bookRepository.findById(id);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            checkedBook.setAvailable(true);
            checkedBook.setReplenishmentDate(new Date());
            bookRepository.update(checkedBook);
            closeRequests(checkedBook);
            updateOrders(checkedBook);
            return true;
        }
        return false;
    }

    @Override
    public boolean writeOffBook(Long id) {
        Book checkedBook = bookRepository.findById(id);
        if (checkedBook != null && checkedBook.isAvailable()) {
            checkedBook.setAvailable(false);
            bookRepository.update(checkedBook);
            updateOrders(checkedBook);
            return true;
        }
        return false;
    }

    public List<String> findSortedAllBooksByAvailability() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o2.isAvailable().compareTo(o1.isAvailable())))
                .filter(Objects::nonNull)
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByEditionDate() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getEditionDate().compareTo(o2.getEditionDate())))
                .filter(Objects::nonNull)
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedBooksByPrice() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getPrice().compareTo(o2.getPrice())))
                .filter(Objects::nonNull)
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByReplenishmentDate() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> {
                            if (o1.getReplenishmentDate() == null && o2.getReplenishmentDate() == null) {
                                return 0;
                            } else if (o1.getReplenishmentDate() == null) {
                                return 1;
                            } else if (o2.getReplenishmentDate() == null) {
                                return -1;
                            } else {
                                return o1.getReplenishmentDate().compareTo(o2.getReplenishmentDate());
                            }
                        }))
                .filter(Objects::nonNull)
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    public List<String> findSortedAllBooksByTitle() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getTitle().compareTo(o2.getTitle())))
                .filter(Objects::nonNull)
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .filter((order) -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(startDate)
                        && order.getCompletionDate().before(endDate))
                .map(order -> order.getBook().toString())
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();

        List<Book> soldBooks = orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(startDate)
                        && order.getCompletionDate().before(endDate))
                .map(Order::getBook)
                .distinct()
                .collect(Collectors.toList());

        return bookRepository.findAll()
                .stream()
                .filter(book -> !soldBooks.contains(book))
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> findStaleBooks() {
        List<Order> orders = orderRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -STALE_MONTH_NUMBER);
        Date currentDate = new Date();
        Date staleDate = new Date(calendar.getTimeInMillis());

        List<Book> soldBooks = orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(staleDate)
                        && order.getCompletionDate().before(currentDate))
                .map(Order::getBook)
                .distinct()
                .collect(Collectors.toList());

        return bookRepository.findAll()
                .stream()
                .filter(book -> !soldBooks.contains(book) && book.getReplenishmentDate() != null
                        && book.getReplenishmentDate().before(staleDate))
                .map(Book::toString)
                .collect(Collectors.toList());
    }

    @Override
    public BookDescription showBookDescription(Long id) {
        Book checkedBook = bookRepository.findById(id);
        BookDescription bookDescription = null;
        if (checkedBook != null) {
            bookDescription = new BookDescription();
            bookDescription.setTitle(checkedBook.getTitle());
            bookDescription.setEditionDate(checkedBook.getEditionDate());
            bookDescription.setReplenishmentDate(checkedBook.getReplenishmentDate());
        }
        return bookDescription;
    }

    @Override
    public int exportAllBooks(String fileName) {
        int exportedBooksNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            List<String> bookStrings = EntityCvsConverter.getInstance().convertBooks(bookRepository.findAll());
            exportedBooksNumber = FileDataWriter.writeData(path, bookStrings);

        }
        return exportedBooksNumber;
    }

    @Override
    public boolean exportBook(Long bookId, String fileName) {
        if (bookId != null && fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            Book book = bookRepository.findById(bookId);
            if (book != null) {
                List<String> bookStrings = EntityCvsConverter.getInstance().convertBooks(List.of(book));
                return FileDataWriter.writeData(path, bookStrings) != 0;
            }
        }
        return false;
    }

    @Override
    public int importBooks(String fileName) {
        int importedBooksNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.FORMAT_TYPE.getPathConstant();
            List<String> dataStrings = FileDataReader.readData(path);

            List<Book> repoBooks = bookRepository.findAll();
            List<Book> importedBooks = EntityCvsConverter.getInstance().parseBooks(dataStrings);

            for (Book importedBook : importedBooks) {
                if (repoBooks.contains(importedBook)) {
                    bookRepository.update(importedBook);
                    updateOrders(importedBook);
                    updateRequests(importedBook);
                } else {
                    bookRepository.add(importedBook);
                }
                importedBooksNumber++;
            }
        }
        return importedBooksNumber;
    }

    private List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    private void closeRequests(Book book) {
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request != null && request.isActive() && request.getBook().getId().equals(book.getId())) {
                request.setActive(false);
                requestRepository.update(request);
            }
        }
    }

    private void updateRequests(Book book) {
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request.getBook().getId().equals(book.getId())) {
                request.setBook(book);
                if (request.isActive() && book.isAvailable()) {
                    request.setActive(false);
                }
                requestRepository.update(request);
            }
        }
    }

    private void updateOrders(Book book) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getState() == OrderState.CREATED &&
                    order.getBook().getId().equals(book.getId())) {
                order.setBook(book);
                orderRepository.update(order);
            }
        }
    }
}
