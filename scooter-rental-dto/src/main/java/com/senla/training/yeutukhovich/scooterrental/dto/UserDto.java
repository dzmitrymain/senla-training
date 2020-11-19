package com.senla.training.yeutukhovich.scooterrental.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String role;
    private LocalDateTime creationDate;
    private Boolean enabled;
    private ProfileDto profileDto;
}
