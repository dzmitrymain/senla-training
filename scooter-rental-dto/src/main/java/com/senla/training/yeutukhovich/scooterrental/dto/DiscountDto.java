package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountDto {

    private Long id;
    private ModelDto modelDto;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal discount;
}
