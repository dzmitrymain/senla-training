package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.List;

public class EntityPrinter {

    public static void printEntities(List<String> entityStrings) {

        if (entityStrings == null || entityStrings.isEmpty()) {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
            return;
        }

        System.out.println(MessageConstant.DATA_WAS_FOUND.getMessage());
        System.out.println();
        entityStrings.forEach(System.out::println);
    }
}
