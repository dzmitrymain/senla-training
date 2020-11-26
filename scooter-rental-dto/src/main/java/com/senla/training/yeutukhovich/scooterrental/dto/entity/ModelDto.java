package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModelDto extends AbstractEntityDto {

    @NotEmpty
    private String modelName;
    @NotNull
    @Positive
    private Short range;
    @NotNull
    @Positive
    private Short speed;
    @NotNull
    @Positive
    private Short power;

    public ModelDto(Long id,
                    String modelName,
                    Short range,
                    Short speed,
                    Short power) {
        this.id = id;
        this.modelName = modelName;
        this.range = range;
        this.speed = speed;
        this.power = power;
    }
}
