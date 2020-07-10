package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
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
    private boolean requestAutoCloseEnabled;
    @ConfigProperty(type = ConfigProperty.Type.BYTE)
    private byte staleMonthNumber;

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private BookServiceImpl() {

    }

    @Override
    public void replenishBook(Long id) {
        ConfigInjector.injectConfig(this);
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        if (bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        Book checkedBook = bookOptional.get();
        checkedBook.setAvailable(true);
        checkedBook.setReplenishmentDate(new Date());
        bookRepository.update(checkedBook);
        if (requestAutoCloseEnabled) {
            closeRequests(checkedBook);
        }
        updateOrders(checkedBook);
    }

    @Override
    public void writeOffBook(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        if (!bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
        }

        Book book = bookOptional.get();
        book.setAvailable(false);
        bookRepository.update(book);
        updateOrders(book);
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
        ConfigInjector.injectConfig(this);
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
    public Optional<BookDescription> showBookDescription(Long id) {
        Optional<Book> bookOptional = bookRepository.findById(id);
        if (bookOptional.isEmpty()) {
            return Optional.empty();
        }
        Book book = bookOptional.get();
        BookDescription bookDescription = new BookDescription();
        bookDescription.setTitle(book.getTitle());
        bookDescription.setEditionDate(book.getEditionDate());
        bookDescription.setReplenishmentDate(book.getReplenishmentDate());
        return Optional.of(bookDescription);
    }

    @Override
    public int exportAllBooks(String fileName) {
        ConfigInjector.injectConfig(this);
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> bookStrings = entityCvsConverter.convertBooks(bookRepository.findAll());
        return FileDataWriter.writeData(path, bookStrings);
    }

    @Override
    public void exportBook(Long bookId, String fileName) {
        ConfigInjector.injectConfig(this);
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
    }

    @Override
    public int importBooks(String fileName) {
        if (fileName == null) {
            return 0;
        }
        ConfigInjector.injectConfig(this);
        int importedBooksNumber = 0;
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
        return importedBooksNumber;
    }

    private List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    private void closeRequests(Book book) {
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (request == null || !request.isActive() || !request.getBook().getId().equals(book.getId())) {
                continue;
            }
            request.setActive(false);
            requestRepository.update(request);
        }
    }

    private void updateRequests(Book book) {
        List<Request> requests = requestRepository.findAll();
        for (Request request : requests) {
            if (!request.getBook().getId().equals(book.getId())) {
                continue;
            }
            request.setBook(book);
            if (request.isActive() && book.isAvailable()) {
                request.setActive(false);
            }
            requestRepository.update(request);
        }
    }

    private void updateOrders(Book book) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getState() != OrderState.CREATED ||
                    !order.getBook().getId().equals(book.getId())) {
                continue;
            }
            order.setBook(book);
            orderRepository.update(order);
        }
    }
}
