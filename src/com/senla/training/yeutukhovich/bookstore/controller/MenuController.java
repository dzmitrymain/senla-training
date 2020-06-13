package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.controller.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.controller.menu.navigator.MenuNavigator;
import com.senla.training.yeutukhovich.bookstore.controller.menu.util.reader.InputReader;

public class MenuController {

    private MenuBuilder menuBuilder;
    private MenuNavigator menuNavigator;

    public MenuController(MenuBuilder menuBuilder) {
        this.menuBuilder = menuBuilder;
        menuBuilder.buildMenu();
        menuNavigator = new MenuNavigator(menuBuilder.getRootMenu());
    }

    public void run() {
        boolean runCheck = true;
        Integer userChoice;

        while (runCheck) {
            menuNavigator.printMenu();

            userChoice = InputReader.readInputInteger();

            if (userChoice != null && (userChoice > 0 && userChoice <= menuNavigator.getCurrentMenu().getMenuItems().size())) {
                menuNavigator.navigate(userChoice);
            } else {
                System.err.println("Please enter the correct number.");
            }

            if (menuNavigator.getCurrentMenu() == null) {
                runCheck = false;
            }
        }
    }
}
