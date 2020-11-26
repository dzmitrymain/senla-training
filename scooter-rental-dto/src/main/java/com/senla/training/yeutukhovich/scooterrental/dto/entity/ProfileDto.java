package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.senla.training.yeutukhovich.scooterrental.validator.NestedDtoIdNotNull;
import com.senla.training.yeutukhovich.scooterrental.validator.marker.OnUserCreate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.groups.Default;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProfileDto extends AbstractEntityDto {

    @NotNull(groups = {Default.class, OnUserCreate.class})
    private Long userId;
    @NotNull(groups = {Default.class, OnUserCreate.class})
    @NestedDtoIdNotNull(groups = {Default.class, OnUserCreate.class})
    private LocationDto locationDto;
    @NotBlank(groups = {Default.class, OnUserCreate.class})
    private String name;
    @NotBlank(groups = {Default.class, OnUserCreate.class})
    private String surname;
    @NotNull(groups = {Default.class, OnUserCreate.class})
    @Email(groups = {Default.class, OnUserCreate.class})
    private String email;
    @Pattern(regexp = "[+][0-9]{12}", groups = {Default.class, OnUserCreate.class})
    private String phoneNumber;

    public ProfileDto(Long id,
                      Long userId,
                      LocationDto locationDto,
                      String name,
                      String surname,
                      String email,
                      String phoneNumber) {
        this.id = id;
        this.userId = userId;
        this.locationDto = locationDto;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
}

