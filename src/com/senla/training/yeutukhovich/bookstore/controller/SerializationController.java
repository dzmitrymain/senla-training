package com.senla.training.yeutukhovich.bookstore.controller;

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
        serializationService.serializeBookstore();
    }

    public void deserializeBookstore() {
        serializationService.deserializeBookstore();
    }
}
