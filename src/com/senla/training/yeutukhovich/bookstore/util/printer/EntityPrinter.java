package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.util.List;

public class EntityPrinter {

    public static void printEntity(List<AbstractEntity> entities) {
        entities.forEach(System.out::println);
    }
}
