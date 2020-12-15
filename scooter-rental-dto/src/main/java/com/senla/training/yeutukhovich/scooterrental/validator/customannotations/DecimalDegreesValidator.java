package com.senla.training.yeutukhovich.scooterrental.validator.customannotations;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DecimalDegreesValidator implements ConstraintValidator<DecimalDegrees, Double> {

    @Override
    public boolean isValid(Double aDouble, ConstraintValidatorContext constraintValidatorContext) {
        if (aDouble == null) {
            return true;
        }
        return aDouble >= -180 && aDouble <= 180;
    }
}
