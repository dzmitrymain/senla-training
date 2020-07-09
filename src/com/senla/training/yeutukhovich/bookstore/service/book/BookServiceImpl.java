package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.repository.IBookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IOrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class BookServiceImpl implements BookService {

    @ConfigProperty(type = ConfigProperty.Type.BOOLEAN)
    private Boolean requestAutoCloseEnabled;
    @ConfigProperty(type = ConfigProperty.Type.BYTE)
    private Byte staleMonthNumber;

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private BookServiceImpl() {

    }

    @Override
    public boolean replenishBook(Long id) {
        ConfigInjector.injectConfig(this);
        Book checkedBook = bookRepository.findById(id);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            checkedBook.setAvailable(true);
            checkedBook.setReplenishmentDate(new Date());
            bookRepository.update(checkedBook);
            if (requestAutoCloseEnabled) {
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
        //TODO:Delete
        ConfigInjector.injectConfig(this);
        System.out.println(staleMonthNumber);
        List<Order> orders = orderRepository.findAll();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -staleMonthNumber);
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
        ConfigInjector.injectConfig(this);
        int exportedBooksNumber = 0;
        if (fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            List<String> bookStrings = entityCvsConverter.convertBooks(bookRepository.findAll());
            exportedBooksNumber = FileDataWriter.writeData(path, bookStrings);

        }
        return exportedBooksNumber;
    }

    @Override
    public boolean exportBook(Long bookId, String fileName) {
        ConfigInjector.injectConfig(this);
        if (bookId != null && fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            Book book = bookRepository.findById(bookId);
            if (book != null) {
                List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
                return FileDataWriter.writeData(path, bookStrings) != 0;
            }
        }
        return false;
    }

    @Override
    public int importBooks(String fileName) {
        ConfigInjector.injectConfig(this);
        int importedBooksNumber = 0;
        if (fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            List<String> dataStrings = FileDataReader.readData(path);

            List<Book> repoBooks = bookRepository.findAll();
            List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);

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
