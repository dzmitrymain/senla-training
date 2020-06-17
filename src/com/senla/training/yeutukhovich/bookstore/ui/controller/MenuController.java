package com.senla.training.yeutukhovich.bookstore.ui.controller;

import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class MenuController {

    private static MenuController instance;

    private MenuBuilder menuBuilder;
    private MenuNavigator menuNavigator;

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    private MenuController() {

    }

    public MenuBuilder getMenuBuilder() {
        return menuBuilder;
    }

    public void setMenuBuilder(MenuBuilder menuBuilder) {
        this.menuBuilder = menuBuilder;
    }

    public MenuNavigator getMenuNavigator() {
        return menuNavigator;
    }

    public void setMenuNavigator(MenuNavigator menuNavigator) {
        this.menuNavigator = menuNavigator;
    }

    public void run() {
        menuBuilder.buildMenu();
        menuNavigator.setCurrentMenu(menuBuilder.getRootMenu());

        while (menuNavigator.getCurrentMenu() != null) {
            menuNavigator.printMenu();
            menuNavigator.navigate(InputReader.readInputInteger());
        }
    }
}
