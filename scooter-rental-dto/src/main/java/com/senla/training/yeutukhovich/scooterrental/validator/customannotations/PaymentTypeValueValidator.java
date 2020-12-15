package com.senla.training.yeutukhovich.scooterrental.validator.customannotations;

import com.senla.training.yeutukhovich.scooterrental.domain.type.PaymentType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PaymentTypeValueValidator implements ConstraintValidator<PaymentTypeValue, String> {

    @Override
    public boolean isValid(String aString, ConstraintValidatorContext constraintValidatorContext) {
        if (aString == null) {
            return true;
        }
        try {
            PaymentType.valueOf(aString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
