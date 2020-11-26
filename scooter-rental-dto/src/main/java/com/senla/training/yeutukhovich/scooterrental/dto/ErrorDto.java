package com.senla.training.yeutukhovich.scooterrental.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import javax.validation.ConstraintViolation;
import java.util.List;

@Value
public class ErrorDto {

    HttpStatus status;
    String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    List<FieldError> fieldErrors;

    @Value
    @AllArgsConstructor
    public static class FieldError {

        public FieldError(ConstraintViolation<?> constraintViolation) {
            this.field = StringUtils.substringAfter(
                    constraintViolation.getPropertyPath().toString(),
                    ".");
            this.message = constraintViolation.getMessage();
        }

        String field;
        String message;
    }
}
