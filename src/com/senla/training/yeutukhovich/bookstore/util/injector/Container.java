package com.senla.training.yeutukhovich.bookstore.util.injector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

public class Container {

    private static Set<Object> singletons = new HashSet<>();

    static {
        initSingletons();
        injectDependencies();
    }

    @SuppressWarnings("unchecked")
    public static <T> T getImplementation(Class<T> clazz) {
        for (Object singleton : singletons) {
            if (clazz.isInstance(singleton)) {
                return (T) singleton;
            }
        }
        //TODO: do smth with exception
        throw new RuntimeException("Can't find singleton: " + clazz.getName());
    }

    private static void initSingletons() {
        for (Class singletonClazz : PackageScanner.findSingletons("")) {
            singletons.add(newInstance(singletonClazz));
        }
    }

    private static Object newInstance(Class clazz) {
        Constructor[] constructors = clazz.getDeclaredConstructors();
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount() == 0) {
                constructor.setAccessible(true);
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
        //TODO: do smth with exception
        throw new RuntimeException("Can't create instance");
    }

    private static void injectDependencies() {
        for (Object singleton : singletons) {
            for (Field field : singleton.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    field.setAccessible(true);
                    Object realization = getImplementation(field.getType());
                    if (realization != null) {
                        try {
                            field.set(singleton, realization);
                        } catch (IllegalAccessException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }
}
