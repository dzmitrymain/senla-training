package com.senla.training.yeutukhovich.bookstore.util.injector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class PackageScanner {

    private static final String ROOT_PACKAGE_PATH = "";

    private PackageScanner() {

    }

    public static Set<Class<?>> findSingletons() {
        return getClasses().stream()
                .filter(clazz ->
                        clazz.isAnnotationPresent(Singleton.class))
                .collect(Collectors.toSet());

    }

    private static Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<>();
        Queue<String> packagePathQueue = new PriorityQueue<>();
        packagePathQueue.add(ROOT_PACKAGE_PATH);
        // можно объявить переменную внутри цикла?
        String packagePath;
        while (!packagePathQueue.isEmpty()) {
            packagePath = packagePathQueue.poll();
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
