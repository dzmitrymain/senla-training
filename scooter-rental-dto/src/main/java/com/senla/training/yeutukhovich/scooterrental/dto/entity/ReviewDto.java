package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedDtoIdNotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReviewDto extends AbstractEntityDto {

    @NotNull
    @NestedDtoIdNotNull
    private ProfileDto profileDto;
    @NotNull
    @NestedDtoIdNotNull
    private ModelDto modelDto;
    @NotNull
    @Min(0)
    @Max(5)
    private Byte score;
    private String opinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationDate;

    public ReviewDto(Long id,
                     ProfileDto profileDto,
                     ModelDto modelDto,
                     Byte score,
                     String opinion,
                     LocalDateTime creationDate) {
        this.id = id;
        this.profileDto = profileDto;
        this.modelDto = modelDto;
        this.score = score;
        this.opinion = opinion;
        this.creationDate = creationDate;
    }
}
