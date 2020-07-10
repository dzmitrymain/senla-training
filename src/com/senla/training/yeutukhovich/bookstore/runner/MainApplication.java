package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.controller.SerializationController;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import com.senla.training.yeutukhovich.bookstore.util.injector.Container;

public class MainApplication {

    public static void main(String[] args) {
        try {
            Container.initContainer();
            loadData();

            MenuController menuController = Container.getImplementation(MenuController.class);
            menuController.run();

            UserInputReader.closeReader();

            saveData();
        } catch (InternalException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void loadData() {
        Container.getImplementation(SerializationController.class).deserializeBookstore();
    }

    private static void saveData() {
        Container.getImplementation(SerializationController.class).serializeBookstore();
    }
}
