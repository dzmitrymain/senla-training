package com.senla.training.yeutukhovich.bookstore.runner;


import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;

public class MainApplication {

    public static void main(String[] args) {
        loadData();

        MenuController menuController = MenuController.getInstance();
        menuController.setMenuBuilder(MenuBuilder.getInstance());
        menuController.setMenuNavigator(MenuNavigator.getInstance());
        menuController.run();

        saveData();
    }

    private static void loadData() {
        BookController.getInstance().deserializeBooks();
        OrderController.getInstance().deserializeOrders();
        RequestController.getInstance().deserializeRequests();
    }

    private static void saveData() {
        BookController.getInstance().serializeBooks();
        OrderController.getInstance().serializeOrders();
        RequestController.getInstance().serializeRequests();
    }

}
