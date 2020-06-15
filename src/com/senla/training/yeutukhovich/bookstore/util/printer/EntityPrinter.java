package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.List;

public class EntityPrinter {

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
