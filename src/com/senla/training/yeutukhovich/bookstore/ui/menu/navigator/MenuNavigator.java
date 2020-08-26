package com.senla.training.yeutukhovich.bookstore.ui.menu.navigator;

import com.senla.training.yeutukhovich.bookstore.ui.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.ui.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

@Singleton
public class MenuNavigator {

    private Menu currentMenu;

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println();
        System.out.println(MessageConstant.MENU_BORDER.getMessage());
        System.out.println(MessageConstant.MENU_NAME_INDENT.getMessage() + currentMenu.getName());
        System.out.println(MessageConstant.MENU_ITEM_BORDER.getMessage());
        int firstMenuIndex = 1;
        for (MenuItem menuItem : currentMenu.getMenuItems()) {
            System.out.println(firstMenuIndex++ + MessageConstant.MENU_ITEM_INDEX_DELIMITER.getMessage()
                    + menuItem.getTitle());
        }
        System.out.println(MessageConstant.MENU_BORDER.getMessage());
        System.out.println();
    }

    public void navigate(Integer index) {
        if (index == null || !(index > 0 &&
                index <= currentMenu.getMenuItems().size())) {
            UiConsolePrinter.printError(MessageConstant.ENTER_CORRECT_NUMBER.getMessage());
            return;
        }

        MenuItem selectedMenuItem = currentMenu.getMenuItems().get(--index);
        if (selectedMenuItem.getAction() != null) {
            selectedMenuItem.doAction();
        }
        if (selectedMenuItem.getNextMenu() != null) {
            currentMenu = selectedMenuItem.getNextMenu();
        } else {
            currentMenu = null;
        }
    }
}
