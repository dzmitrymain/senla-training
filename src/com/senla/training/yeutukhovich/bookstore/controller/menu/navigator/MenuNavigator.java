package com.senla.training.yeutukhovich.bookstore.controller.menu.navigator;

import com.senla.training.yeutukhovich.bookstore.controller.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.controller.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

public class MenuNavigator {

    private Menu currentMenu;

    public MenuNavigator() {

    }

    public MenuNavigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {

        System.out.println();
        System.out.println(MessageConstant.MENU_BORDER);
        System.out.println("        "+currentMenu.getName());
        System.out.println(MessageConstant.MENU_ITEM_BORDER);
        int firstMenuIndex = 1;
        for (MenuItem menuItem : currentMenu.getMenuItems()) {
            System.out.println(firstMenuIndex++ + ". " + menuItem.getTitle());
        }
        System.out.println(MessageConstant.MENU_BORDER);
        System.out.println();
    }

    public void navigate(Integer index) {

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
