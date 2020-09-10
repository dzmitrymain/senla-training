package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Container;
import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;

public class MainApplication {

    public static void main(String[] args) {
        MenuController menuController = Container.getImplementation(MenuController.class);
        menuController.run();

        UserInputReader.closeReader();
    }
}
