package com.senla.training.yeutukhovich.bookstore.dependencyinjection;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PackageScanner {

    private static final HashSet<Class<?>> SINGLETONS = new HashSet<>();

    private static final String ROOT_PACKAGE_PATH = "";

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
            throw new InternalException(e.getMessage());
        }
    }

    private PackageScanner() {

    }

    public static Set<Class<?>> findSingletons() {
        return SINGLETONS;

    }

//    public static Set<Class<?>> findSingletons() {
//        return getClasses().stream()
//                .filter(clazz ->
//                        clazz.isAnnotationPresent(Singleton.class))
//                .collect(Collectors.toSet());
//
//    }

    private static Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        Queue<String> packagePathQueue = new PriorityQueue<>();
        packagePathQueue.add(ROOT_PACKAGE_PATH);
        while (!packagePathQueue.isEmpty()) {
            String packagePath = packagePathQueue.poll();
            for (String line : getContentLines(packagePath)) {
                if (line.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packagePath.replace("/", ".")
                                + line.substring(0, line.lastIndexOf('.'))));
                    } catch (ClassNotFoundException e) {
                        throw new InternalException(e.getMessage());
                    }
                } else if (!line.contains(".")) {
                    packagePathQueue.add(packagePath + line + "/");
                }
            }
        }
        return classes;
    }


    private static List<String> getContentLines(String packagePath) {
        URL contextUrl = Thread.currentThread().getContextClassLoader().getResource(packagePath);
        if (contextUrl == null) {
            throw new InternalException("Package not found: " + packagePath);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) contextUrl.getContent()))) {
            return br.lines().collect(Collectors.toList());
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
