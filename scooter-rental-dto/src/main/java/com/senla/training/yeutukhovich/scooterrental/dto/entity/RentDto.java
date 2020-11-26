package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RentDto extends AbstractEntityDto {

    private UserDto userDto;
    private ScooterDto scooterDto;
    private Boolean active;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnDate;
    private String paymentType;
    private Integer distanceTravelled;
    private BigDecimal price;
    private BigDecimal overtimePenalty;

    public RentDto(Long id,
                   UserDto userDto,
                   ScooterDto scooterDto,
                   Boolean active,
                   LocalDateTime creationDate,
                   LocalDateTime expiredDate,
                   LocalDateTime returnDate,
                   String paymentType,
                   Integer distanceTravelled,
                   BigDecimal price,
                   BigDecimal overtimePenalty) {
        this.id = id;
        this.userDto = userDto;
        this.scooterDto = scooterDto;
        this.active = active;
        this.creationDate = creationDate;
        this.expiredDate = expiredDate;
        this.returnDate = returnDate;
        this.paymentType = paymentType;
        this.distanceTravelled = distanceTravelled;
        this.price = price;
        this.overtimePenalty = overtimePenalty;
    }
}
