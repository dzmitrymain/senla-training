package com.senla.training.yeutukhovich.bookstore.dto.auth;

import org.springframework.http.HttpStatus;

public class RegistrationResponseDto {

    private HttpStatus status;
    private String username;
    private String role;

    public String getUsername() {
        return username;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
