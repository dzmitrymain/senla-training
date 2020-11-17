package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpotDto {

    private Long id;
    private String locationName;
    private String phoneNumber;
    private Double latitude;
    private Double longitude;
}
