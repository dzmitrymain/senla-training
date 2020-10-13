package com.senla.training.yeutukhovich.bookstore.dto;

import org.springframework.http.HttpStatus;

public class ErrorDto {

    private HttpStatus status;
    private String message;

    public ErrorDto() {

    }

    public ErrorDto(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
