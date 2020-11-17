package com.senla.training.yeutukhovich.scooterrental.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {

    private Long id;
    private String modelName;
    private Short range;
    private Short speed;
    private Short power;
}
