package com.senla.training.yeutukhovich.scooterrental.validator;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.AbstractEntityDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NestedDtoIdNotNullValidator implements ConstraintValidator<NestedDtoIdNotNull, AbstractEntityDto> {

    @Override
    public boolean isValid(AbstractEntityDto abstractEntityDto, ConstraintValidatorContext constraintValidatorContext) {
        if(abstractEntityDto==null){
            return true;
        }
        return abstractEntityDto.getId() != null;
    }
}
