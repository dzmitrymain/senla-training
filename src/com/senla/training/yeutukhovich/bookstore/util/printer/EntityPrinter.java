package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.List;

public class EntityPrinter {

    // не использовать сырые типы, использовать дженерики (обязательно!)
    // если дженерик тип заранее не известен, можно написать
    // List<?> - это называется вайлкард, что равносильно
    // List<Object> (но так не пишут)
    // кроме того, все твои энтити расширяют класс AbstractEntity - можно использовать его
    // в качестве дженерик типа
    public static void printEntities(List entities) {

        if (!entities.isEmpty()) {
            System.out.println(MessageConstant.DATA_WAS_FOUND);
            System.out.println();
            for (Object entity : entities) {
                System.out.println(entity.toString());
            }
        } else {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND);
        }
    }
}
