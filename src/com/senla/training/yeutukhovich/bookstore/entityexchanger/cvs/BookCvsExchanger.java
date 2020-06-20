package com.senla.training.yeutukhovich.bookstore.entityexchanger.cvs;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.AbstractCvsExchanger;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.EntityExchanger;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.util.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.List;

public class BookCvsExchanger extends AbstractCvsExchanger implements EntityExchanger<Book> {

    private static EntityExchanger<Book> instance;

    private IRepository<Book> bookRepository = BookRepository.getInstance();
    private IRepository<Order> orderRepository = OrderRepository.getInstance();
    private IRepository<Request> requestRepository = RequestRepository.getInstance();

    private BookCvsExchanger() {

    }

    public static EntityExchanger<Book> getInstance() {
        if (instance == null) {
            instance = new BookCvsExchanger();
        }
        return instance;
    }

    @Override
    public void exportEntities(List<Book> books, String fileName) {
        List<String> bookStrings = EntityCvsConverter.convertBooks(books);
        if (!bookStrings.isEmpty()) {
            FileDataWriter.writeData(buildPath(fileName), bookStrings);
        }
    }

    @Override
    public int importEntities(String fileName) {
        int importedBooksNumber = 0;
        List<String> dataStrings = FileDataReader.readData(buildPath(fileName));

        List<Book> repoBooks = bookRepository.findAll();
        List<Book> importedBooks = EntityCvsConverter.parseBooks(dataStrings);

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

    private void updateOrders(Book book) {
        List<Order> orders = orderRepository.findAll();
        for (Order order : orders) {
            if (order.getBook().getId().equals(book.getId())) {
                order.setBook(book);
                orderRepository.update(order);
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
}
