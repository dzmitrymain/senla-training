package com.senla.training.yeutukhovich.bookstore.ui.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

public class UiConsolePrinter {

    private UiConsolePrinter(){

    }

    public static void printMessage(String message) {
        if (message.isEmpty()) {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
        } else {
            System.out.println(message);
        }
    }

    public static void printError(String errorMessage) {
        System.err.println(errorMessage);
    }
}
