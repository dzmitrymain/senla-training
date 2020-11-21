package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpotDtoMapper {

    private final LocationDtoMapper locationDtoMapper;
    private final GeometryFactory geometryFactory;

    @Autowired
    public SpotDtoMapper(LocationDtoMapper locationDtoMapper, GeometryFactory geometryFactory) {
        this.locationDtoMapper = locationDtoMapper;
        this.geometryFactory = geometryFactory;
    }


    public SpotDto map(Spot spot) {
        if (spot == null) {
            return null;
        }
        return new SpotDto(
                spot.getId(),
                locationDtoMapper.map(spot.getLocation()),
                spot.getPhoneNumber(),
                spot.getCoordinates() == null ? null : spot.getCoordinates().getY(),
                spot.getCoordinates() == null ? null : spot.getCoordinates().getX());
    }

    public Spot map(SpotDto spotDto) {
        if (spotDto == null) {
            return null;
        }
        Spot spot = new Spot();
        spot.setId(spotDto.getId());
        spot.setLocation(locationDtoMapper.map(spotDto.getLocationDto()));
        spot.setPhoneNumber(spotDto.getPhoneNumber());
        spot.setCoordinates(geometryFactory.createPoint(new Coordinate(spotDto.getLongitude(), spotDto.getLatitude())));
        return spot;
    }
}
