package com.senla.training.yeutukhovich.bookstore.util.injector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Container {

    private static Set<Object> singletons = new HashSet<>();
    private static boolean initMethodCalled;

    private Container() {

    }

    public static void initContainer() {
        initMethodCalled = true;
        initSingletons();
        injectDependencies();
    }

    public static <T> T getImplementation(Class<T> clazz) {
        return getImplementation(clazz, "");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getImplementation(Class<T> clazz, String defaultClassSimpleName) {
        if (!initMethodCalled) {
            throw new InternalException(MessageConstant.CONTAINER_NOT_INIT.getMessage());
        }
        List<Object> implementations = singletons.stream()
                .filter(clazz::isInstance)
                .collect(Collectors.toList());
        long implementationsNumber = implementations.size();

        if (implementationsNumber == 0) {
            throw new InternalException(MessageConstant.CANT_FIND_SINGLETON.getMessage());
        }
        if (implementationsNumber == 1) {
            return (T) implementations.get(0);
        }
        if (defaultClassSimpleName.isEmpty()) {
            throw new InternalException(String.format(MessageConstant.AMBIGUITY_CHOICE.getMessage()
                    , clazz.getSimpleName()));
        }
        Optional<Object> implementationOptional = implementations.stream()
                .filter(implementation -> implementation.getClass().getSimpleName().equals(defaultClassSimpleName))
                .findAny();
        if (implementationOptional.isEmpty()) {
            throw new InternalException(MessageConstant.CANT_FIND_SINGLETON.getMessage());
        }
        return (T) implementationOptional.get();
    }

    private static void initSingletons() {
        PackageScanner.findSingletons("")
                .forEach(singleton -> singletons.add(newInstance(singleton)));
    }

    private static Object newInstance(Class clazz) {
        String errorMessage = "";
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                constructor.setAccessible(true);
                try {
                    return constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    errorMessage = e.getMessage();
                }
            }
        }
        throw new InternalException(errorMessage);
    }

    private static void injectDependencies() {
        for (Object singleton : singletons) {
            for (Field field : singleton.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    Autowired autowired = field.getAnnotation(Autowired.class);
                    field.setAccessible(true);
                    Object realization;
                    if (autowired.defaultClassSimpleName().equals("")) {
                        realization = getImplementation(field.getType());
                    } else {
                        realization = getImplementation(field.getType(), autowired.defaultClassSimpleName());
                    }
                    try {
                        field.set(singleton, realization);
                    } catch (IllegalAccessException e) {
                        throw new InternalException(e.getMessage());
                    }

                }
                if (field.isAnnotationPresent(ConfigProperty.class)) {
                    ConfigInjector.injectConfig(field, singleton);
                }
            }
        }
    }
}
