package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

public class MenuController {

    private MenuBuilder menuBuilder;
    private MenuNavigator menuNavigator;

    public MenuController(MenuBuilder menuBuilder, MenuNavigator menuNavigator) {
        this.menuBuilder = menuBuilder;
        this.menuNavigator = menuNavigator;

    }

    public void run() {
        menuBuilder.buildMenu();
        menuNavigator.setCurrentMenu(menuBuilder.getRootMenu());

        boolean runCheck = true;
        Integer userChoice;

        while (runCheck) {
            menuNavigator.printMenu();

            userChoice = InputReader.readInputInteger();

            if (userChoice != null && (userChoice > 0 &&
                    userChoice <= menuNavigator.getCurrentMenu().getMenuItems().size())) {
                menuNavigator.navigate(userChoice);
            } else {
                System.err.println(MessageConstant.ENTER_CORRECT_NUMBER);
            }

            if (menuNavigator.getCurrentMenu() == null) {
                runCheck = false;
            }
        }
    }
}
