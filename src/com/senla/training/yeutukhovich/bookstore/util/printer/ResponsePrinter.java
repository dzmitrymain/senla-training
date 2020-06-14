package com.senla.training.yeutukhovich.bookstore.util.printer;

import java.util.ArrayList;
import java.util.List;

public class ResponsePrinter {

    public static void printResponse(Object... messages) {

        System.out.println();
        System.out.println("********************");
        for (Object string : messages) {
            System.out.println(string);
        }
        System.out.println("********************");
    }

    public static void printEntities(List entities) {

        List<String> data = new ArrayList<>();
        if (!entities.isEmpty()) {
            data.add("Data was found: ");
            data.add("");
            for (Object entity : entities) {
                data.add(entity.toString());
            }
        } else {
            data.add("No data was found.");
        }
        printResponse(data.toArray());
    }
}
