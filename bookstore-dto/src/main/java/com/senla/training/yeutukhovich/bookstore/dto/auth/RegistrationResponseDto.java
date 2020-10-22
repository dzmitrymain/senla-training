package com.senla.training.yeutukhovich.bookstore.dto.auth;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class RegistrationResponseDto {

    private HttpStatus status;
    private String username;
    private String role;
}
