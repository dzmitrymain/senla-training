package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;

import java.util.Comparator;

public interface RequestService {

    Request createRequest(Book book, String requesterData);

    Request[] findBookRequests(Book book, Comparator<Request> requestComparator);
}
