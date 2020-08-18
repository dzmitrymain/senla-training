package com.senla.training.yeutukhovich.bookstore.util.injector;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.*;

public class Container {

    private static final Map<Class<?>, Object> singletons = new HashMap<>();

    static {
        initContainer();
        System.out.println();
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
        PackageScanner.findSingletons()
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
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                boolean tempAccessible = constructor.canAccess(null);
                constructor.setAccessible(true);
                try {
                    return (T) constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    throw new InternalException(e.getMessage());
                } finally {
                    constructor.setAccessible(tempAccessible);
                }
            }
        }
        throw new InternalException("Constructor without parameters not exists. " + clazz.getSimpleName());
    }

    private static void injectDependencies() {
        singletons.values().forEach(Container::injectDependenciesInObject);
    }

    private static void injectDependenciesInObject(Object singleton) {
        for (Field field : getInheritedFields(singleton.getClass())) {
            if (field.isAnnotationPresent(Autowired.class)) {
                boolean tempAccessible = field.canAccess(singleton);
                field.setAccessible(true);
                try {
                    field.set(singleton, getImplementation(field.getType()));
                } catch (IllegalAccessException e) {
                    throw new InternalException(e.getMessage());
                } finally {
                    field.setAccessible(tempAccessible);
                }
            }
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigInjector.injectConfig(field, singleton);
            }
        }
    }

    private static List<Field> getInheritedFields(Class<?> clazz) {
        List<Field> result = new ArrayList<>();
        Class<?> i = clazz;
        while (i != null && i != Object.class) {
            result.addAll(Arrays.asList(i.getDeclaredFields()));
            i = i.getSuperclass();
        }
        return result;
    }
}
