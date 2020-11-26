package com.senla.training.yeutukhovich.scooterrental.handler;

import com.senla.training.yeutukhovich.scooterrental.dto.ErrorDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleBusinessException(BusinessException exception) {
        return ResponseEntity.status(exception.getHttpStatus()).body(new ErrorDto(exception.getHttpStatus(), exception.getMessage(), null));
    }

//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<ErrorDto> handleAccessDeniedException() {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(HttpStatus.FORBIDDEN,
//                MessageConstant.ACCESS_DENIED.getMessage()));
//    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDto> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDto(HttpStatus.FORBIDDEN, exception.getMessage(), null));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolationException(ConstraintViolationException exception) {
        List<ErrorDto.FieldError> errors = exception.getConstraintViolations().stream()
                .map(ErrorDto.FieldError::new)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(HttpStatus.BAD_REQUEST,
                ExceptionConstant.VALIDATION_FAILURE.getMessage(), errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleAllExceptions(Exception exception) {
        log.error(exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR, ExceptionConstant.SOMETHING_WENT_WRONG.getMessage(), null));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             HttpHeaders headers,
                                                             HttpStatus status,
                                                             WebRequest request) {
        if (body == null) {
            body = new ErrorDto(status, ex.getMessage(), null);
        }
        return ResponseEntity.status(status)
                .headers(headers)
                .body(body);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<ErrorDto.FieldError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new ErrorDto.FieldError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return this.handleExceptionInternal(ex, new ErrorDto(status, ExceptionConstant.VALIDATION_FAILURE.getMessage(), errors),
                headers, status, request);
    }
}
