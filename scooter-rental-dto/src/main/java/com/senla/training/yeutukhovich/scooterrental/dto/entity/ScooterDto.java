package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedDtoIdNotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ScooterDto extends AbstractEntityDto {

    @NotNull
    @NestedDtoIdNotNull
    private ModelDto modelDto;
    @NotNull
    @NestedDtoIdNotNull
    private SpotDto spotDto;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginOperationDate;

    public ScooterDto(Long id, ModelDto modelDto, SpotDto spotDto, LocalDateTime beginOperationDate) {
        this.id = id;
        this.modelDto = modelDto;
        this.spotDto = spotDto;
        this.beginOperationDate = beginOperationDate;
    }
}
