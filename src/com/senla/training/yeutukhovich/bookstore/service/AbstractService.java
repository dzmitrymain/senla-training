package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.dao.connector.DBConnector;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;

public abstract class AbstractService {

    @Autowired
    protected DBConnector connector;
}
