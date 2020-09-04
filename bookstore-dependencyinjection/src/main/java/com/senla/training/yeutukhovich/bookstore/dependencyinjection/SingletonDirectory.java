package com.senla.training.yeutukhovich.bookstore.dependencyinjection;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.exception.ApplicationContextException;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class SingletonDirectory {

    private static final HashSet<Class<?>> SINGLETONS = new HashSet<>();

    static {
        try {
            SINGLETONS.addAll(Arrays.asList(
                    Class.forName("com.senla.training.yeutukhovich.bookstore.controller.OrderController"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.controller.RequestController"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.controller.BookController"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.ui.menu.navigator.MenuNavigator"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.service.order.OrderServiceImpl"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.service.book.BookServiceImpl"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.service.request.RequestServiceImpl"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.ui.menu.builder.MenuBuilder"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.dao.request.RequestDaoImpl"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.ui.controller.MenuController"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.dao.connector.DbConnector"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.dao.book.BookDaoImpl"),
                    Class.forName("com.senla.training.yeutukhovich.bookstore.dao.order.OrderDaoImpl")
            ));
        } catch (ClassNotFoundException e) {
            throw new ApplicationContextException(e.getMessage());
        }
    }

    private SingletonDirectory() {

    }

    public static Set<Class<?>> getSingletons() {
        return SINGLETONS;
    }
}
