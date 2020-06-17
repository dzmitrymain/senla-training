package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder;
import com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

// меню контроллер и контроллеры бэкенда - это разные контроллеры
// меню контроллер - это чать UI, должен лежать в пакете UI
// просто контроллеры - в пакете контроллер, они сами вызывают методы сервисов, а их самих вызывают экшены
// экшены тоже часть UI и должны лежать там
// может быть синглтон?
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

            // нашел проверку на размер, хорошо, что она есть, но лучше, если она будет в навигаторе
            // хотя на самом деле не принципиально
            // просто добавляя проверку в конечный класс, ты обезопасишь его использование другими разработчиками
            if (userChoice != null && (userChoice > 0 &&
                    userChoice <= menuNavigator.getCurrentMenu().getMenuItems().size())) {
                menuNavigator.navigate(userChoice);
            } else {
                System.err.println(MessageConstant.ENTER_CORRECT_NUMBER);
            }

            // можно было обойтись без лишней булен переменной, если запихнуть этот стейтмент сразу в вайл
            // while(menuNavigator.getCurrentMenu() != null)
            if (menuNavigator.getCurrentMenu() == null) {
                runCheck = false;
            }
        }
    }
}
