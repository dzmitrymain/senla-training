package com.senla.training.yeutukhovich.scooterrental.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDto {

    private Long id;
    private Long userId;
    private LocationDto locationDto;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
}

