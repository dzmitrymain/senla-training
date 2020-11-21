package com.senla.training.yeutukhovich.scooterrental.service.spot;

import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;

import java.util.List;
import java.util.Map;

public interface SpotService {

    List<SpotDto> findAll();

    SpotDto findById(Long id);

    SpotDto deleteById(Long id);

    SpotDto updateById(Long id, SpotDto spotDto);

    SpotDto create(SpotDto spotDto);

    List<Map<String, Long>> findDistancesToSpots(Double latitude, Double longitude);

    List<ScooterDto> findAvailableScootersBySpotId(Long id);

    List<ScooterDto> findSpotScooters(Long id);
}
