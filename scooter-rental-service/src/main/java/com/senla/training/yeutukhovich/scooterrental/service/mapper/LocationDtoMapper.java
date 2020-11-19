package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Location;
import com.senla.training.yeutukhovich.scooterrental.dto.LocationDto;
import org.springframework.stereotype.Component;

@Component
public class LocationDtoMapper {

    public LocationDto map(Location location) {
        if (location == null) {
            return null;
        }
        return new LocationDto(location.getId(), location.getLocationName());
    }

    public Location map(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }
        Location location = new Location();
        location.setId(locationDto.getId());
        location.setLocationName(locationDto.getLocationName());
        return location;
    }
}
