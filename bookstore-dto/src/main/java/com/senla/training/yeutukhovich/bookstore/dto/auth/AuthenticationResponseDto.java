package com.senla.training.yeutukhovich.bookstore.dto.auth;

import org.springframework.http.HttpStatus;

public class AuthenticationResponseDto {

    private HttpStatus status;
    private String token;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
