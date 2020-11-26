package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocationDto extends AbstractEntityDto {

    @NotBlank
    private String locationName;

    public LocationDto(Long id, String locationName) {
        this.id = id;
        this.locationName = locationName;
    }
}
