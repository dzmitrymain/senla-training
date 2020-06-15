package com.senla.training.yeutukhovich.bookstore.service.impl;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.EntityRepository;
import com.senla.training.yeutukhovich.bookstore.service.RequestService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
            return request;
        }
        return null;
    }

    @Override
    public List<Request> findAllRequests(Comparator<Request> requestComparator) {
        List<Request> requests = requestRepository.findAll();
        return requests.stream().sorted(requestComparator).collect(Collectors.toList());
    }
}
