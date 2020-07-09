package com.senla.training.yeutukhovich.bookstore.util.injector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class PackageScanner {
    private static Set<Class> classes;

    public static Set<Class> findSingletons(String packageName) {
        return findClasses(packageName).stream()
                .filter(clazz ->
                        clazz.isAnnotationPresent(Singleton.class))
                .collect(Collectors.toSet());

    }

    private static Set<Class> findClasses(String packageName) {
        classes = new HashSet<>();
        scanPackage(packageName);
        System.out.println();
        return classes;
    }

    private static void scanPackage(String packageName) {
        URL contextUrl = Thread.currentThread().getContextClassLoader().getResource(packageName);
        if (contextUrl == null) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) contextUrl.getContent()))) {
            br.lines().forEach(line -> {
                if (line.endsWith(".class")) {
                    try {
                        classes.add(Class.forName(packageName.replaceAll("/", ".")
                                + line.substring(0, line.lastIndexOf('.'))));
                    } catch (ClassNotFoundException e) {
                        System.err.println(e.getMessage());
                    }
                }
                if (!line.contains(".")) {
                    scanPackage(packageName + line + "/");
                }
            });
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
