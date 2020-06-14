package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;

import java.util.Arrays;
import java.util.Comparator;

public class RequestServiceImpl implements RequestService {

    private static RequestServiceImpl instance;

    private EntityRepository<Book> bookRepository;
    private EntityRepository<Request> requestRepository;

    private RequestServiceImpl(EntityRepository<Book> bookRepository,
                               EntityRepository<Request> requestRepository) {
        this.bookRepository = bookRepository;
        this.requestRepository = requestRepository;
    }

    public static RequestServiceImpl getInstance() {
        if (instance == null) {
            instance = new RequestServiceImpl(
                    EntityRepository.getBookRepositoryInstance(),
                    EntityRepository.getRequestRepositoryInstance()
            );
        }
        return instance;
    }

    @Override
    public Request createRequest(Long bookId, String requesterData) {
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            Request request = new Request(checkedBook, requesterData);
            requestRepository.add(request);
            System.out.println("Request {" + checkedBook.getTitle() + "} has been created.");
            return request;
        }
        return null;
    }

    @Override
    public Request[] findAllRequests(Comparator<Request> requestComparator) {
        Request[] requests = requestRepository.findAll();
        Arrays.sort(requests, requestComparator);
        return requests;
    }
}