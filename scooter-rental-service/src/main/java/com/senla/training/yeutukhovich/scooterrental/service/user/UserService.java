package com.senla.training.yeutukhovich.scooterrental.service.user;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RegistrationRequestDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.marker.OnUserCreate;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.marker.OnUserUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

public interface UserService {

    @Validated(OnUserCreate.class)
    UserDto register(@Valid RegistrationRequestDto registrationRequestDto);

    List<UserDto> findAll();

    UserDto findById(Long id);

    UserDto deleteById(Long id);

    @Validated(OnUserUpdate.class)
    UserDto updateById(Long id, @Valid UserDto userDto);

    UserDto changePasswordByUserId(Long id, String oldPassword, String newPassword);

    List<RentDto> findSortedByCreationDateUserRents(Long id);

    List<PassDto> findAllActiveUserPasses(Long id);
}
