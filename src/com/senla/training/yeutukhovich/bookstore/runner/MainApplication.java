package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;

public class MainApplication {

    public static void main(String[] args) {
        MenuController menuController = new MenuController(new MenuBuilder(), new MenuNavigator());
        menuController.run();
    }
}
