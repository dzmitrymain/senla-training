package com.senla.training.yeutukhovich.scooterrental.service.location;

import com.senla.training.yeutukhovich.scooterrental.dto.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;

import java.util.List;

public interface LocationService {

    List<LocationDto> findSortedAllLocationsByName();

    LocationDto findById(Long id);

    LocationDto deleteById(Long id);

    LocationDto updateById(Long id, LocationDto locationDto);

    LocationDto create(LocationDto locationDto);

    List<ProfileDto> findLocationProfiles(Long id);

    List<SpotDto> findLocationSpots(Long id);
}
