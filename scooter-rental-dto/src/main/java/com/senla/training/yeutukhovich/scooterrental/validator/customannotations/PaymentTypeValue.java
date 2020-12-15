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
@Constraint(validatedBy = PaymentTypeValueValidator.class)
@Documented
public @interface PaymentTypeValue {

    String message() default "{paymentType.value}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
