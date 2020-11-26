package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedDtoIdNotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DiscountDto extends AbstractEntityDto {

    @NotNull
    @NestedDtoIdNotNull
    private ModelDto modelDto;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime endDate;
    @Positive
    @Max(100)
    @NotNull
    private BigDecimal discount;

    public DiscountDto(Long id,
                       ModelDto modelDto,
                       LocalDateTime startDate,
                       LocalDateTime endDate,
                       BigDecimal discount) {
        this.id = id;
        this.modelDto = modelDto;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discount = discount;
    }
}
