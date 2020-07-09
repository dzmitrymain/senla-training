package com.senla.training.yeutukhovich.bookstore.util.injector.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD, ElementType.LOCAL_VARIABLE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ConfigProperty {

    String configName() default "configuration.properties";

    String propertyName() default "ClassName.FieldName";

    Type type() default Type.STRING;

    enum Type {
        STRING,
        BOOLEAN,
        BYTE;
    }
}
