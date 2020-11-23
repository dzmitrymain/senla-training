package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.registry.infomodel.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassDto {

    private Long id;
    private UserDto userDto;
    private ModelDto modelDto;
    private LocalDateTime creationDate;
    private LocalDateTime expiredDate;
    private Integer totalMinutes;
    private Integer remainingMinutes;
    private BigDecimal price;
}
