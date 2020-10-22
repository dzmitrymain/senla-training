package com.senla.training.yeutukhovich.bookstore.model.service.user;

import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationRequestDto;
import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationResponseDto;

public interface UserService {

    RegistrationResponseDto addUser(RegistrationRequestDto requestDto);
}
