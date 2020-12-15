package com.senla.training.yeutukhovich.scooterrental.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.customannotations.PaymentTypeValue;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CreationRentDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long scooterId;
    @NotNull
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredDate;
    @NotNull
    @PaymentTypeValue
    private String paymentType;
}
