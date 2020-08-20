package com.senla.training.yeutukhovich.bookstore.service.book;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.domain.state.OrderState;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.AbstractService;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

//TODO: create DAO SELECT methods
@Singleton
public class BookServiceImpl extends AbstractService implements BookService {

    @ConfigProperty
    private boolean requestAutoCloseEnabled;
    @ConfigProperty
    private byte staleMonthNumber;

    private BookServiceImpl() {

    }

    @Override
    public void replenishBook(Long id) {
        Connection connection = connector.getConnection();
        Optional<Book> bookOptional = bookDao.findById(connection, id);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        if (bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        Book checkedBook = bookOptional.get();
        checkedBook.setAvailable(true);
        checkedBook.setReplenishmentDate(new Date());
        try {
            connection.setAutoCommit(false);
            try {
                bookDao.update(connection, checkedBook);
                if (requestAutoCloseEnabled) {
                    closeRequests(connection, checkedBook);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public void writeOffBook(Long id) {
        Connection connection = connector.getConnection();
        Optional<Book> bookOptional = bookDao.findById(connection, id);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        if (!bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
        }
        Book book = bookOptional.get();
        book.setAvailable(false);
        bookDao.update(connection, book);
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
        Connection connection = connector.getConnection();
        List<Order> orders = orderDao.findAll(connection);
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
        Connection connection = connector.getConnection();
        List<Order> orders = orderDao.findAll(connection);
        List<Book> soldBooks = orders.stream()
                .filter(order -> order.getState() == OrderState.COMPLETED
                        && order.getCompletionDate().after(startDate)
                        && order.getCompletionDate().before(endDate))
                .map(Order::getBook)
                .distinct()
                .collect(Collectors.toList());
        return bookDao.findAll(connection)
                .stream()
                .filter(book -> !soldBooks.contains(book))
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> findStaleBooks() {
        Connection connection = connector.getConnection();
        List<Order> orders = orderDao.findAll(connection);
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
        return bookDao.findAll(connection)
                .stream()
                .filter(book -> !soldBooks.contains(book) && book.getReplenishmentDate() != null
                        && book.getReplenishmentDate().before(staleDate))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDescription> showBookDescription(Long id) {
        Connection connection = connector.getConnection();
        Optional<Book> bookOptional = bookDao.findById(connection, id);
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
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> bookStrings = entityCvsConverter.convertBooks(bookDao.findAll(connection));
        return FileDataWriter.writeData(path, bookStrings);
    }

    @Override
    public void exportBook(Long bookId, String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Book> bookOptional = bookDao.findById(connection, bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
        FileDataWriter.writeData(path, bookStrings);
    }

    //TODO: import bug
    @Override
    public int importBooks(String fileName) {
        Connection connection = connector.getConnection();
        if (fileName == null) {
            return 0;
        }
        int importedBooksNumber = 0;
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> repoBooks = bookDao.findAll(connection);
        List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);
        try {
            connection.setAutoCommit(false);
            try {
                for (Book importedBook : importedBooks) {
                    if (repoBooks.contains(importedBook)) {
                        bookDao.update(connection, importedBook);
                        updateRequestsAfterImport(importedBook);
                    } else {
                        bookDao.add(connection, importedBook);
                    }
                    importedBooksNumber++;
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
        return importedBooksNumber;
    }

    private List<Book> findAllBooks() {
        Connection connection = connector.getConnection();
        return bookDao.findAll(connection);
    }

    private void closeRequests(Connection connection, Book book) {
        //TODO: change SELECT
        List<Request> requests = requestDao.findAll(connection);
        for (Request request : requests) {
            if (request == null || !request.isActive() || !request.getBook().getId().equals(book.getId())) {
                continue;
            }
            request.setActive(false);
            requestDao.update(connection, request);
        }
    }

    private void updateRequestsAfterImport(Book book) {
        Connection connection = connector.getConnection();
        List<Request> requests = requestDao.findAll(connection);
        for (Request request : requests) {
            if (!request.getBook().getId().equals(book.getId())) {
                continue;
            }
            if (request.isActive() && book.isAvailable()) {
                request.setActive(false);
                requestDao.update(connection, request);
            }
        }
    }
}
