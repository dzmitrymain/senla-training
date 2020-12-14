package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    public UserDto register(@RequestBody RegistrationRequestDto registrationRequestDto) {
        return userService.register(registrationRequestDto);
    }

    @GetMapping
    public List<UserDto> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public UserDto deleteById(@PathVariable("id") Long id) {
        return userService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public UserDto updateById(@PathVariable("id") Long id, @RequestBody UserDto userDto) {
        return userService.updateById(id, userDto);
    }

    @PostMapping("/{id}/password")
    public UserDto changePasswordByUserId(@PathVariable("id") Long id,
                                          @RequestParam("old") String oldPassword,
                                          @RequestParam("new") String newPassword) {
        return userService.changePasswordByUserId(id, oldPassword, newPassword);
    }

    @GetMapping("/{id}/rents")
    public List<RentDto> findRentsByUserId(@PathVariable("id") Long id) {
        return userService.findSortedByCreationDateUserRents(id);
    }

    @GetMapping("/{id}/activePasses")
    public List<PassDto> findAllActivePassesByUserId(@PathVariable("id") Long id) {
        return userService.findAllActiveUserPasses(id);
    }
}
