package com.senla.training.yeutukhovich.bookstore.ui.util.printer;

import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

public class UiConsolePrinter {

    // форматируй код (или настрой кодстайл) - обычно пустая строка не ставится после открывающей метод скобки
    public static void printMessage(String message) {

        if (message.isEmpty()) {
            System.out.println(MessageConstant.NO_DATA_WAS_FOUND.getMessage());
        } else {
            System.out.println(message);
        }
    }
}
