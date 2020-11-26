package com.senla.training.yeutukhovich.scooterrental.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = NestedDtoIdNotNullValidator.class)
@Documented
public @interface NestedDtoIdNotNull {

    String message() default "{id.notnull}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
