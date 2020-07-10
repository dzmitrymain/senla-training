package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.service.serialization.SerializationService;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

@Singleton
public class SerializationController {

    @Autowired
    private SerializationService serializationService;

    private SerializationController() {

    }

    public void serializeBookstore() {
        try {
            serializationService.serializeBookstore();
        } catch (InternalException e) {
            //log e.getMessage();
        }
    }

    public void deserializeBookstore() {
        try {
            serializationService.deserializeBookstore();
        } catch (InternalException e) {
            //log e.getMessage();
        }
    }
}
