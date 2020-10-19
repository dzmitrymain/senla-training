package com.senla.training.yeutukhovich.bookstore.dto.auth;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class AuthenticationResponseDto {

    private HttpStatus status;
    private String token;
}
