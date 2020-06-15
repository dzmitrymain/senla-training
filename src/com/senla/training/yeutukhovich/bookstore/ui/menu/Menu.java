package com.senla.training.yeutukhovich.bookstore.ui.menu;

import java.util.ArrayList;
import java.util.List;

public class Menu {

    private String name;
    private List<MenuItem> menuItems;

    {
        menuItems = new ArrayList<>();
    }

    public Menu() {

    }

    public Menu(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

}
