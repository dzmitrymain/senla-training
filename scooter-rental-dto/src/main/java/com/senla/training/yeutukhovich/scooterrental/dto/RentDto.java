package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentDto {

    private Long id;
    private UserDto userDto;
    private ScooterDto scooterDto;
    private Boolean active;
    private LocalDateTime creationDate;
    private LocalDateTime expiredDate;
    private LocalDateTime returnDate;
    private String paymentType;
    private Integer distanceTravelled;
    private BigDecimal price;
    private BigDecimal overtimePenalty;
}
