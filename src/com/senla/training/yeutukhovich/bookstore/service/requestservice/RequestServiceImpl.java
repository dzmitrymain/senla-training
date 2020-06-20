package com.senla.training.yeutukhovich.bookstore.service.requestservice;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RequestServiceImpl implements RequestService {

    private static RequestService instance;

    private IRepository<Book> bookRepository;
    private IRepository<Request> requestRepository;

    private RequestServiceImpl() {
        this.bookRepository = BookRepository.getInstance();
        this.requestRepository = RequestRepository.getInstance();
    }

    public static RequestService getInstance() {
        if (instance == null) {
            instance = new RequestServiceImpl();
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
        return requests.stream()
                .sorted(requestComparator)
                .collect(Collectors.toList());
    }
}
