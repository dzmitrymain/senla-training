package com.senla.training.yeutukhovich.bookstore.runner;

import com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApplication {

    //TODO: properties in hibernate config


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(
                "com.senla.training.yeutukhovich.bookstore");
        MenuController menuController = context.getBean(MenuController.class);
        menuController.run();

        UserInputReader.closeReader();
    }
}
