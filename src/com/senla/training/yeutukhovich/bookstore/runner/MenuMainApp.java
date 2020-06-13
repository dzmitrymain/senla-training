package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.controller.menu.builder.MenuBuilder;

public class MenuMainApp {

    public static void main(String[] args) {
        MenuController menuController = new MenuController(new MenuBuilder());
        menuController.run();
    }
}
