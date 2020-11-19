package com.senla.training.yeutukhovich.scooterrental.service.scooter;

import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;

import java.util.List;

public interface ScooterService {

    List<ScooterDto> findAll();

    ScooterDto findById(Long id);

    ScooterDto deleteById(Long id);

    ScooterDto updateById(Long id, ScooterDto scooterDto);

    ScooterDto create(ScooterDto scooterDto);

    Integer findDistanceTravelledByScooterId(Long id);

    List<RentDto> findSortedByCreationScooterRents(Long id);
}
