package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Object> handleBusinessException(
            BusinessException exception, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", exception.getMessage());

        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception exception, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("message", MessageConstant.SOMETHING_WENT_WRONG.getMessage());
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
