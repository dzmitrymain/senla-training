package com.senla.training.yeutukhovich.bookstore.util.injector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Container {

    private static final Map<Class<?>, Object> singletons = new HashMap<>();


    static {
        initContainer();
    }

    private Container() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T getImplementation(Class<T> clazz) {
        return (T) singletons.get(clazz);
    }


    private static void initContainer() {
        initSingletons();
        injectDependencies();
    }

    private static void initSingletons() {
        PackageScanner.findSingletons("")
                .forEach(singletonClass -> {
                    Object singletonInstance = newInstance(singletonClass);
                    for (Type interfaceType : singletonClass.getGenericInterfaces()) {
                        singletons.put((Class<?>) interfaceType, singletonInstance);
                    }
                    singletons.put(singletonClass, singletonInstance);
                });
    }

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> clazz) {
        String errorMessage = "";
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                constructor.setAccessible(true);
                try {
                    return (T) constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    errorMessage = e.getMessage();
                } finally {
                    constructor.setAccessible(false);
                }
            }
        }
        throw new InternalException(errorMessage);
    }

    private static void injectDependencies() {
        singletons.values().stream()
                .distinct()
                .forEach(singleton -> {
                    for (Field field : singleton.getClass().getDeclaredFields()) {
                        if (field.isAnnotationPresent(Autowired.class)) {
                            field.setAccessible(true);
                            try {
                                field.set(singleton, getImplementation(field.getType()));
                            } catch (IllegalAccessException e) {
                                throw new InternalException(e.getMessage());
                            } finally {
                                field.setAccessible(false);
                            }
                        }
                        if (field.isAnnotationPresent(ConfigProperty.class)) {
                            ConfigInjector.injectConfig(field, singleton);
                        }
                    }
                });
    }
}
