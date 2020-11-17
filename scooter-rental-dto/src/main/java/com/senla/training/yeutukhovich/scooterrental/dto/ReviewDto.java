package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {

    private Long id;
    private ProfileDto profileDto;
    private ModelDto modelDto;
    private Byte score;
    private String opinion;
    private LocalDateTime creationDate;
}
