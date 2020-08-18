package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.dao.BookDao;
import com.senla.training.yeutukhovich.bookstore.dao.OrderDao;
import com.senla.training.yeutukhovich.bookstore.dao.RequestDao;
import com.senla.training.yeutukhovich.bookstore.dao.connector.DBConnector;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;

public abstract class AbstractService {

    @Autowired
    protected DBConnector connector;

    @Autowired
    protected BookDao bookDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected RequestDao requestDao;
}
