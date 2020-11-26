package com.senla.training.yeutukhovich.scooterrental.dto;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import com.senla.training.yeutukhovich.scooterrental.validator.marker.OnUserCreate;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
public class RegistrationRequestDto {

    @Valid
    private UserDto userDto;
    @NotBlank(groups = OnUserCreate.class)
    private String password;
}
