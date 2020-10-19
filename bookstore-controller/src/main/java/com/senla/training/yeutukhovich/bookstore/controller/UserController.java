package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationRequestDto;
import com.senla.training.yeutukhovich.bookstore.dto.auth.RegistrationResponseDto;
import com.senla.training.yeutukhovich.bookstore.model.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public RegistrationResponseDto registerUser(@RequestBody RegistrationRequestDto requestDto) {
        return userService.addUser(requestDto);
    }
}
