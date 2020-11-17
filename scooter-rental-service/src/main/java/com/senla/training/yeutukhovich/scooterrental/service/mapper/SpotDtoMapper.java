package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;
import org.springframework.stereotype.Component;

@Component
public class SpotDtoMapper {

    public SpotDto map(Spot spot) {
        if (spot == null) {
            return null;
        }
        return new SpotDto(
                spot.getId(),
                spot.getLocation() == null ? null : spot.getLocation().getLocationName(),
                spot.getPhoneNumber(),
                spot.getCoordinates() == null ? null : spot.getCoordinates().getX(),
                spot.getCoordinates() == null ? null : spot.getCoordinates().getY());
    }
}
