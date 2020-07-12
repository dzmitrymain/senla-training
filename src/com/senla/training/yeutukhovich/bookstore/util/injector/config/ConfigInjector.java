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
        loadStream(configProperty.configName());
        String property = getProperty(configProperty.propertyName(), field);
        if (property == null) {
            throw new InternalException(String.format(MessageConstant.CANT_FIND_PROPERTY.getMessage(),
                    configProperty.propertyName(), configProperty.configName()));
        }
        Object castedProperty = castProperty(property, configProperty.type());
        field.setAccessible(true);
        try {
            field.set(object, castedProperty);
        } catch (IllegalAccessException e) {
            throw new InternalException(e.getMessage());
        }
    }

    private static String getProperty(String propertyName, Field field) {
        if (propertyName.isEmpty()) {
            return PROPERTIES.getProperty(field.getDeclaringClass().getSimpleName() + "." + field.getName());
        }
        return PROPERTIES.getProperty(propertyName);
    }

    // нарушено условие задание: при дефолтном значении атрибута ConfigProperty.Type type
    // необходимо определить тип из поля, над которым стоит аннотация ConfigProperty
    private static Object castProperty(String property, ConfigProperty.Type type) {
        if (ConfigProperty.Type.STRING == type) {
            return property;
        }
        if (ConfigProperty.Type.BYTE == type) {
            return Byte.parseByte(property);
        }
        if (ConfigProperty.Type.BOOLEAN == type) {
            return Boolean.parseBoolean(property);
        }
        return property;
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
