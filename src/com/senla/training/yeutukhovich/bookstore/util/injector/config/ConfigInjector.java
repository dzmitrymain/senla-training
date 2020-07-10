package com.senla.training.yeutukhovich.bookstore.util.injector.config;

import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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

    public static void injectConfig(Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(ConfigProperty.class)) {
                ConfigProperty configProperty = field.getAnnotation(ConfigProperty.class);
                loadStream(configProperty.configName());
                String property = getProperty(configProperty.propertyName(), field);
                if (property == null) {
                    //TODO:literal?
                    System.err.println("Can't find property: '" + configProperty.propertyName()
                            + "' at properties file: '" + configProperty.configName() + "'");
                    continue;
                }
                Object castedProperty = castProperty(property, configProperty.type());
                field.setAccessible(true);
                try {
                    field.set(object, castedProperty);
                } catch (IllegalAccessException e) {
                    System.err.println(e.getMessage());
                }
            }
        }
    }

    private static String getProperty(String propertyName, Field field) {
        if ("ClassName.FieldName".equals(propertyName)) {
            return PROPERTIES.getProperty(field.getDeclaringClass().getSimpleName() + "." + field.getName());
        }
        return PROPERTIES.getProperty(propertyName);
    }

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
            System.err.println(e.getMessage());
        }
    }
}
