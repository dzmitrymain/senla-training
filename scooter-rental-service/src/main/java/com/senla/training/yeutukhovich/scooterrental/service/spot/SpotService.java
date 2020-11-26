package com.senla.training.yeutukhovich.scooterrental.service.spot;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.validator.DecimalDegrees;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface SpotService {

    List<SpotDto> findAll();

    SpotDto findById(Long id);

    SpotDto deleteById(Long id);

    SpotDto updateById(Long id, @Valid SpotDto spotDto);

    SpotDto create(@Valid SpotDto spotDto);

    List<Map<String, Long>> findDistancesToSpots(@DecimalDegrees Double latitude, @DecimalDegrees Double longitude);

    List<ScooterDto> findAvailableScootersBySpotId(Long id);

    List<ScooterDto> findSpotScooters(Long id);
}
