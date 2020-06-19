package com.senla.training.yeutukhovich.bookstore.util.printer;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

import java.util.List;

public class EntityPrinter {

    public static <T extends AbstractEntity> void printEntities(List<T> entities) {

        if (entities != null && !entities.isEmpty()) {
            System.out.println(MessageConstant.DATA_WAS_FOUND.getMessage());
            System.out.println();
            entities.forEach(System.out::println);
        } else {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
        }
    }
}
