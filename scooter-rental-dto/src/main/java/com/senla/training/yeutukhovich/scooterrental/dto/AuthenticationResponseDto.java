package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class AuthenticationResponseDto {

    HttpStatus status;
    String token;
}
