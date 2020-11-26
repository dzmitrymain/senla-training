package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PassDto extends AbstractEntityDto {

    @NotNull
    @NestedEntityDto
    private UserDto userDto;
    @NotNull
    @NestedEntityDto
    private ModelDto modelDto;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;
    @Future
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiredDate;
    @NotNull
    @Positive
    private Integer totalMinutes;
    private Integer remainingMinutes;
    private BigDecimal price;

    public PassDto(Long id,
                   UserDto userDto,
                   ModelDto modelDto,
                   LocalDateTime creationDate,
                   LocalDateTime expiredDate,
                   Integer totalMinutes,
                   Integer remainingMinutes,
                   BigDecimal price) {
        this.id = id;
        this.userDto = userDto;
        this.modelDto = modelDto;
        this.creationDate = creationDate;
        this.expiredDate = expiredDate;
        this.totalMinutes = totalMinutes;
        this.remainingMinutes = remainingMinutes;
        this.price = price;
    }
}
