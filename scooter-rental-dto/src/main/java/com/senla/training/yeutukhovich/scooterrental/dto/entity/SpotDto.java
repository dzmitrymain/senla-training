package com.senla.training.yeutukhovich.scooterrental.dto.entity;

import com.senla.training.yeutukhovich.scooterrental.validator.DecimalDegrees;
import com.senla.training.yeutukhovich.scooterrental.validator.NestedEntityDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpotDto extends AbstractEntityDto {

    @NotNull
    @NestedEntityDto
    private LocationDto locationDto;
    @NotBlank
    @Pattern(regexp = "[+][0-9]{12}")
    private String phoneNumber;
    @NotNull
    @DecimalDegrees
    private Double latitude;
    @NotNull
    @DecimalDegrees
    private Double longitude;

    public SpotDto(Long id, LocationDto locationDto, String phoneNumber, Double latitude, Double longitude) {
        this.id = id;
        this.locationDto = locationDto;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
