package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.*;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.configuration.ConfigurationData;
import com.senla.training.yeutukhovich.bookstore.util.constant.PathConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.*;
import java.util.stream.Collectors;

public class BookServiceImpl implements BookService {

    private static final Boolean REQUEST_AUTO_CLOSE = Boolean.valueOf(
            ConfigurationData.getValue(ConfigurationData.REQUEST_AUTO_CLOSE));
    private static final byte STALE_MONTH_NUMBER = Byte.parseByte(
            ConfigurationData.getValue(ConfigurationData.STALE_MONTH_NUMBER));

    private static BookService instance;

    private IBookRepository bookRepository = BookRepository.getInstance();
    private IOrderRepository orderRepository = OrderRepository.getInstance();
    private IRequestRepository requestRepository = RequestRepository.getInstance();

    private BookServiceImpl() {

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
            if (REQUEST_AUTO_CLOSE) {
                closeRequests(checkedBook);
            }
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

    public List<Book> findSortedAllBooksByAvailability() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o2.isAvailable().compareTo(o1.isAvailable())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Book> findSortedAllBooksByEditionDate() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getEditionDate().compareTo(o2.getEditionDate())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Book> findSortedBooksByPrice() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getPrice().compareTo(o2.getPrice())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Book> findSortedAllBooksByReplenishmentDate() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> {
                            if (o1.getReplenishmentDate() == null && o2.getReplenishmentDate() == null) {
                                return 0;
                            }
                            if (o1.getReplenishmentDate() == null) {
                                return 1;
                            }
                            if (o2.getReplenishmentDate() == null) {
                                return -1;
                            }
                            return o1.getReplenishmentDate().compareTo(o2.getReplenishmentDate());
                        }))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public List<Book> findSortedAllBooksByTitle() {
        return findAllBooks().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getTitle().compareTo(o2.getTitle())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(startDate)
                        && order.getCompletionDate().before(endDate))
                .map(Order::getBook)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
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
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findStaleBooks() {

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
                .collect(Collectors.toList());
    }

    @Override
    public BookDescription showBookDescription(Long id) {
        Book checkedBook = bookRepository.findById(id);

        if (checkedBook == null) {
            return null;
        }

        BookDescription bookDescription = new BookDescription();
        bookDescription.setTitle(checkedBook.getTitle());
        bookDescription.setEditionDate(checkedBook.getEditionDate());
        bookDescription.setReplenishmentDate(checkedBook.getReplenishmentDate());
        return bookDescription;
    }

    @Override
    public int exportAllBooks(String fileName) {
        int exportedBooksNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
            List<String> bookStrings = EntityCvsConverter.getInstance().convertBooks(bookRepository.findAll());
            exportedBooksNumber = FileDataWriter.writeData(path, bookStrings);

        }
        return exportedBooksNumber;
    }

    @Override
    public boolean exportBook(Long bookId, String fileName) {
        if (bookId != null && fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
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
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
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
