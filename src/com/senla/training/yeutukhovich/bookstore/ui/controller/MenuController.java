package com.senla.training.yeutukhovich.bookstore.ui.controller;

import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

@Singleton
public class MenuController {

    @Autowired
    private MenuBuilder menuBuilder;
    @Autowired
    private MenuNavigator menuNavigator;

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
            menuNavigator.navigate(UserInputReader.readInputInteger());
        }
    }
}
