package com.senla.training.yeutukhovich.bookstore.util.injector.config;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Properties;

public class ConfigInjector {

    private static final Properties PROPERTIES;

    static {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty(PropertyKeyConstant.STALE_MONTH_NUMBER, "6");
        defaultProperties.setProperty(PropertyKeyConstant.REQUEST_AUTO_CLOSE_ENABLED, "true");

        defaultProperties.setProperty(PropertyKeyConstant.CVS_DIRECTORY_KEY, "resources/cvs/");
        defaultProperties.setProperty(PropertyKeyConstant.SERIALIZATION_DATA_PATH_KEY,
                "resources/data/ApplicationState.dat");
        PROPERTIES = new Properties(defaultProperties);
    }

    private ConfigInjector() {

    }

    public static void injectConfig(Object object) {
        Arrays.stream(object.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(ConfigProperty.class))
                .forEach(field -> injectConfig(field, object));
    }

    public static void injectConfig(Field field, Object object) {
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        // очень абстрактное название для метода, который подгружает настройки (лоадПроперти получше будет)
        // текущий метод injectConfig() вызывается в цикле, будет ли целесообразно вызывать подгрузку
        // настроек каждый раз в каждом цикле?
        loadStream(configProperty.configName());
        String property = getProperty(configProperty.propertyName(), field);
        if (property == null) {
            throw new InternalException(String.format(MessageConstant.CANT_FIND_PROPERTY.getMessage(),
                    configProperty.propertyName(), configProperty.configName()));
        }
        try {
            Object castedProperty = castProperty(property, configProperty.type(), field.getType());
            field.setAccessible(true);
            field.set(object, castedProperty);
        } catch (ClassCastException | IllegalAccessException e) {
            throw new InternalException(e.getMessage());
        } finally {
            // сетать исходное значение
            field.setAccessible(false);
        }
    }

    private static String getProperty(String propertyName, Field field) {
        if (propertyName.isEmpty()) {
            return PROPERTIES.getProperty(field.getDeclaringClass().getSimpleName() + "." + field.getName());
        }
        return PROPERTIES.getProperty(propertyName);
    }

    private static Object castProperty(String property, ConfigProperty.Type type, Class<?> fieldType) {
        String fieldTypeString = type.toString().toUpperCase();
        // значения енама можно сравнивать через двойное равно (так читается лучше и надежней)
        // а кастинг к типу поля вынести в другой метод и делать сравнение
        // Class<?> fieldType == Integer.class
        // то есть идти не от строковых имен, а все же от равенства объектов
        // не принципиально, но надежней и наглядней
        if (ConfigProperty.Type.DEFAULT.toString().equals(fieldTypeString)) {
            fieldTypeString = fieldType.getSimpleName().toUpperCase();
        }
        if (ConfigProperty.Type.STRING.toString().equals(fieldTypeString)) {
            return property;
        }
        if (ConfigProperty.Type.BYTE.toString().equals(fieldTypeString)) {
            return Byte.parseByte(property);
        }
        if (ConfigProperty.Type.BOOLEAN.toString().equals(fieldTypeString)) {
            return Boolean.parseBoolean(property);
        }
        throw new ClassCastException("Not supported class cast operation for type: " + fieldType.getSimpleName());
    }

    private static void loadStream(String propertiesPath) {
        if (propertiesPath == null) {
            return;
        }
        try (InputStream stream = new FileInputStream(
                ApplicationConstant.RESOURCE_ROOT_FOLDER.getConstant() + "/" + propertiesPath)) {
            PROPERTIES.load(stream);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
