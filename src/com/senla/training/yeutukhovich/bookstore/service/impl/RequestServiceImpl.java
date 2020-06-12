package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;

import java.util.Arrays;
import java.util.Comparator;

public class RequestServiceImpl implements RequestService {

    private EntityRepository<Book> bookRepository;
    private EntityRepository<Request> requestRepository;

    public RequestServiceImpl(EntityRepository<Book> bookRepository,
                              EntityRepository<Request> requestRepository) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public Request createRequest(Book book, String requesterData) {
        if (book != null) {
            Book checkedBook = bookRepository.findById(book.getId());
            if (checkedBook != null && !checkedBook.isAvailable()) {
                Request request = new Request(checkedBook, requesterData);
                requestRepository.add(request);
                System.out.println("Request {" + checkedBook.getTitle() + "} has been created.");
                return request;
            }
        }
        return null;
    }

    @Override
    public Request[] findBookRequests(Book book, Comparator<Request> requestComparator) {
        Request[] requests = requestRepository.findAll();
        Arrays.sort(requests, requestComparator);
        System.out.println("Book requests {" + book.getTitle() + "} has been found.");
        return requestRepository.findAll();
    }
}
