package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.List;

public class EntityPrinter {

    public static <T extends AbstractEntity> void printEntities(List<T> entities) {

        // если поменять местами тела у ифа и елса, из условия пропадут унарные операторы, и читать станет легче
        // а если добавить ретурн в иф, то елс вообще можно будет не писать:

        /*if (entities == null || entities.isEmpty()) {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
            return;
        }

        System.out.println(MessageConstant.DATA_WAS_FOUND.getMessage());
        System.out.println();
        entities.forEach(System.out::println);*/

        // так немного легче читать

        if (entities != null && !entities.isEmpty()) {
            System.out.println(MessageConstant.DATA_WAS_FOUND.getMessage());
            System.out.println();
            entities.forEach(System.out::println);
        } else {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
        }
    }

}
