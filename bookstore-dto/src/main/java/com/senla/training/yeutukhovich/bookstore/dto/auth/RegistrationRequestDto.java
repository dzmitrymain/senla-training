package com.senla.training.yeutukhovich.bookstore.dto.auth;

import lombok.Data;

@Data
public class RegistrationRequestDto {

    private String username;
    private String password;
    private String role;
}
