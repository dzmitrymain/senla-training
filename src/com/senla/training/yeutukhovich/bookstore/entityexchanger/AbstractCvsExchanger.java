package com.senla.training.yeutukhovich.bookstore.entityexchanger;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsconverter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;

public abstract class AbstractCvsExchanger {

    protected static EntityCvsConverter entityCvsConverter = EntityCvsConverter.getInstance();

    protected static IRepository<Book> bookRepository = BookRepository.getInstance();
    protected static IRepository<Order> orderRepository = OrderRepository.getInstance();
    protected static IRepository<Request> requestRepository = RequestRepository.getInstance();

    private static final String directoryPath = "./resources/cvs/";
    private static final String fileFormat = ".cvs";

    protected String buildPath(String fileName) {
        return directoryPath + fileName + fileFormat;
    }
}
