package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScooterDto {

    private Long id;
    private ModelDto modelDto;
    private SpotDto spotDto;
    private LocalDateTime beginOperationDate;
}
