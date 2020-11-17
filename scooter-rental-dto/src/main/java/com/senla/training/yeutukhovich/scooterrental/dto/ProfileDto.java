package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private Long profileId;
    private String username;
    private String role;
    private Boolean enabled;
    private LocalDateTime creationDate;

    private String locationName;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}

