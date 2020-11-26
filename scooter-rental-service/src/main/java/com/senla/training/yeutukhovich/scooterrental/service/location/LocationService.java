package com.senla.training.yeutukhovich.scooterrental.service.location;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;

import javax.validation.Valid;
import java.util.List;

public interface LocationService {

    List<LocationDto> findSortedAllLocationsByName();

    LocationDto findById(Long id);

    LocationDto deleteById(Long id);

    LocationDto updateById(Long id, @Valid LocationDto locationDto);

    LocationDto create(@Valid LocationDto locationDto);

    List<ProfileDto> findLocationProfiles(Long id);

    List<SpotDto> findLocationSpots(Long id);
}
