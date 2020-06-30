package com.senla.training.yeutukhovich.bookstore.runner;


import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;

public class MainApplication {

    public static void main(String[] args) {
        BookstoreSerializer bookstoreSerializer = BookstoreSerializer.getInstance();

        bookstoreSerializer.deserializeBookstore();

        MenuController menuController = MenuController.getInstance();
        menuController.setMenuBuilder(MenuBuilder.getInstance());
        menuController.setMenuNavigator(MenuNavigator.getInstance());
        menuController.run();

        bookstoreSerializer.serializeBookstore();
    }
}
