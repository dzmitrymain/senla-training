package com.senla.training.yeutukhovich.scooterrental.service.user;

import com.senla.training.yeutukhovich.scooterrental.dto.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto register(RegistrationRequestDto registrationRequestDto);

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto deleteById(Long id);

    UserDto updateById(Long id, UserDto userDto);

    UserDto changePasswordByUserId(Long id, String oldPassword, String newPassword);

    List<RentDto> findSortedByCreationDateUserRents(Long id);

    List<PassDto> findAllActiveUserPasses(Long id);
}
