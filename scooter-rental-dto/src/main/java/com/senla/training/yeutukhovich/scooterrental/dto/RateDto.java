package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateDto {

    private Long id;
    private ModelDto modeldto;
    private BigDecimal perHour;
    private BigDecimal weekendPerHour;
    private LocalDateTime creationDate;
}
