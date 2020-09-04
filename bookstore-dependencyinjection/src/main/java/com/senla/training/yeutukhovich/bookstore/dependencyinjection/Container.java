package com.senla.training.yeutukhovich.bookstore.dependencyinjection;

import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.PostConstruct;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class Container {

    private static final Logger LOGGER = LoggerFactory.getLogger(Container.class);

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
        try {
            initSingletons();
            injectDependencies();
            invokeInits();
        } catch (InternalException e) {
            LOGGER.error(e.getMessage(), e);
            throw e;
        }
    }

    private static void initSingletons() {
        SingletonDirectory.getSingletons()
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

    private static void invokeInits() {
        singletons.values().forEach(Container::invokeInitsInObject);
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

    private static void invokeInitsInObject(Object singleton) {
        for (Method method : singleton.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(PostConstruct.class)) {
                method.setAccessible(true);
                try {
                    method.invoke(singleton);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new InternalException(e.getMessage());
                }
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
