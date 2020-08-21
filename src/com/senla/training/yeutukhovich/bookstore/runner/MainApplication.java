package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.dao.connector.DBConnector;
import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import com.senla.training.yeutukhovich.bookstore.util.injector.Container;

public class MainApplication {

    public static void main(String[] args) {
        MenuController menuController = Container.getImplementation(MenuController.class);
        menuController.run();

        UserInputReader.closeReader();
        Container.getImplementation(DBConnector.class).closeConnection();
    }
}
