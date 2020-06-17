package com.senla.training.yeutukhovich.bookstore.ui.menu.navigator;

import com.senla.training.yeutukhovich.bookstore.ui.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.ui.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;

// может быть синглтон?
public class MenuNavigator {

    private Menu currentMenu;

    public MenuNavigator() {

    }

    // конструктор не используется
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
        System.out.println("        " + currentMenu.getName());
        System.out.println(MessageConstant.MENU_ITEM_BORDER);
        int firstMenuIndex = 1;
        for (MenuItem menuItem : currentMenu.getMenuItems()) {
            System.out.println(firstMenuIndex++ + ". " + menuItem.getTitle());
        }
        System.out.println(MessageConstant.MENU_BORDER);
        System.out.println();
    }

    public void navigate(Integer index) {
        // неплохо бы добавить проверку, чтобы не вывалиться за пределы коллекции
        // может проверка есть в другом методе? я еще не смотрел, но лучше проверять прямо тут
        MenuItem selectedMenuItem = currentMenu.getMenuItems().get(--index);

        if (selectedMenuItem.getAction() != null) {
            selectedMenuItem.doAction();
        }

        // все это можно объединить в одну строку (NPE не будет):
        // currentMenu = selectedMenuItem.getNextMenu();
        if (selectedMenuItem.getNextMenu() != null) {
            currentMenu = selectedMenuItem.getNextMenu();
        } else {
            currentMenu = null;
        }
    }
}
