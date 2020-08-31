package com.senla.training.yeutukhovich.bookstore.dependencyinjection.config;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Properties;

public class ConfigInjector {

    private static final Properties PROPERTIES;
    private static String lastPropertyFileName;

    static {
        Properties defaultProperties = new Properties();
        defaultProperties.setProperty(PropertyKeyConstant.STALE_MONTH_NUMBER, "6");
        defaultProperties.setProperty(PropertyKeyConstant.REQUEST_AUTO_CLOSE_ENABLED, "true");

        defaultProperties.setProperty(PropertyKeyConstant.CVS_DIRECTORY_KEY, "resources/cvs/");
        PROPERTIES = new Properties(defaultProperties);
    }

    private ConfigInjector() {

    }

    public static void injectConfig(Field field, Object object) {
        ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
        if (!configProperty.configName().equals(lastPropertyFileName)) {
            loadProperty(configProperty.configName());
        }
        String property = getProperty(configProperty.propertyName(), field);
        if (property == null) {
            throw new InternalException(String.format(MessageConstant.CANT_FIND_PROPERTY.getMessage(),
                    configProperty.propertyName(), configProperty.configName()));
        }
        boolean tempAccessible = field.canAccess(object);
        try {
            Object castedProperty = castProperty(property, configProperty.type(), field.getType());
            field.setAccessible(true);
            field.set(object, castedProperty);
        } catch (ClassCastException | IllegalAccessException e) {
            throw new InternalException(e.getMessage());
        } finally {
            field.setAccessible(tempAccessible);
        }
    }

    private static String getProperty(String propertyName, Field field) {
        if (propertyName.isEmpty()) {
            return PROPERTIES.getProperty(field.getDeclaringClass().getSimpleName() + "." + field.getName());
        }
        return PROPERTIES.getProperty(propertyName);
    }

    private static Object castProperty(String property, ConfigProperty.Type type, Class<?> fieldType) {
        if (ConfigProperty.Type.DEFAULT == type) {
            try {
                ConfigProperty.Type.valueOf(fieldType.getSimpleName().toUpperCase());
                return castDefinedTypeProperty(property, fieldType);
            } catch (IllegalArgumentException e) {
                throw new InternalException("Not supported class cast operation for type: " + fieldType.getSimpleName());
            }
        }
        return castDefinedTypeProperty(property, fieldType);
    }

    private static Object castDefinedTypeProperty(String property, Class<?> fieldType) {
        if (fieldType == String.class) {
            return property;
        }
        if (fieldType == byte.class) {
            return Byte.parseByte(property);
        }
        if (fieldType == boolean.class) {
            return Boolean.parseBoolean(property);
        }
        return null;
    }


    private static void loadProperty(String propertiesPath) {
        if (propertiesPath == null) {
            return;
        }
        try (InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propertiesPath)) {
            if (stream == null) {
                throw new IOException("Can't find properties file: " + propertiesPath);
            }
            PROPERTIES.load(stream);
            lastPropertyFileName = propertiesPath;
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }
}
