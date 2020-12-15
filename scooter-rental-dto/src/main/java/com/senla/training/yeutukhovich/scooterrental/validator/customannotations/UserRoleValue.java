package com.senla.training.yeutukhovich.scooterrental.validator.customannotations;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = UserRoleValueValidator.class)
@Documented
public @interface UserRoleValue {

    String message() default "{userRole.value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
