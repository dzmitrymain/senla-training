package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedDtoIdNotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RateDto extends AbstractEntityDto {

    @NotNull
    @NestedDtoIdNotNull
    private ModelDto modelDto;
    @NotNull
    @Positive
    private BigDecimal perHour;
    @NotNull
    @Positive
    private BigDecimal weekendPerHour;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public RateDto(Long id,
                   ModelDto modelDto,
                   BigDecimal perHour,
                   BigDecimal weekendPerHour,
                   LocalDateTime creationDate) {
        this.id = id;
        this.modelDto = modelDto;
        this.perHour = perHour;
        this.weekendPerHour = weekendPerHour;
        this.creationDate = creationDate;
    }
}
