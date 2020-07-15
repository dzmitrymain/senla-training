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
        // лучше месседж для каждой ситуации подставлять сразу в выбрасываемое исключение, так
        // не придется искать по коду, чему же в итоге равен месседж
        // то есть экономия приводит к запутанности кода
        // вообще нормально, когда исключение выбрасывается с прописанным в коде собощением (литералом)
        // это служит своего рода пояснением-комментарием, почему тут выброс исключения
        // к примеру, я возможно ошибаюсь, но если в классе не будет дефолтного конструктора,
        // а только конструкторы с параметрами, то выбросится InternalException с пустым месседжем
        // в последней строчке метода
        // ниже я укажу точки, где лучше выбрасывать исключения
        String errorMessage = "";
        // Constructor - дженерик класс
        for (Constructor constructor : clazz.getDeclaredConstructors()) {
            if (constructor.getParameterCount() == 0) {
                constructor.setAccessible(true);
                try {
                    return (T) constructor.newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    // выбрасывать исключение нужно тут (с готовым сообщением или кастомным)
                    // throw new InternalException(e.getMessage());
                    errorMessage = e.getMessage();
                } finally {
                    // нужно возвращать прежнее значение accessible - то, которое было до
                    // constructor.setAccessible(true);
                    constructor.setAccessible(false);
                }
            }
        }
        // выбрасывать исключение нужно тут с кастомным сообщением "не найден дефолтный конструктор"
        throw new InternalException(errorMessage);
    }

    private static void injectDependencies() {
        singletons.values().stream()
                // мне кажется, в этом нет необходимости - пусть инъекции приходят во все бины,
                // иначе в мапе окажутся бины-дубли без инъекций, которые в теории может выдать
                // твой контейнер, будет очень долгий и сложный дебаг
                // но как мне видится, дублей там быть не должно
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
                                // сетаем прежнее значение (его надо сохранить в переменную)
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
