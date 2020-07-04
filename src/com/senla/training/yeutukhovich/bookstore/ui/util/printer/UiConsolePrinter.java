package com.senla.training.yeutukhovich.bookstore.ui.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

public class UiConsolePrinter {

    public static void printMessage(String message) {

        if (message.isEmpty()) {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
        } else {
            System.out.println(message);
        }
    }
}
