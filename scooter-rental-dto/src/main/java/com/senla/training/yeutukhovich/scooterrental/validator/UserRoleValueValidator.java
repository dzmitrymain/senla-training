package com.senla.training.yeutukhovich.scooterrental.validator;

import com.senla.training.yeutukhovich.scooterrental.domain.Role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserRoleValueValidator implements ConstraintValidator<UserRoleValue, String> {

    @Override
    public boolean isValid(String aString, ConstraintValidatorContext constraintValidatorContext) {
        if (aString == null) {
            return true;
        }
        try {
            Role.valueOf(aString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
