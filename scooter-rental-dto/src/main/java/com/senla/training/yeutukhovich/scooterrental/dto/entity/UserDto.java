package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.UserRoleValue;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.marker.OnUserCreate;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.marker.OnUserUpdate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDto extends AbstractEntityDto {

    @NotBlank(groups = OnUserCreate.class)
    private String username;
    @NotBlank(groups = {OnUserUpdate.class, OnUserCreate.class})
    @UserRoleValue(groups = {OnUserUpdate.class, OnUserCreate.class})
    private String role;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @NotNull(groups = OnUserUpdate.class)
    private Boolean enabled;
    @Valid
    private ProfileDto profileDto;
}
