package com.senla.training.yeutukhovich.scooterrental.service.scooter;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;

import javax.validation.Valid;
import java.util.List;

public interface ScooterService {

    List<ScooterDto> findAll();

    ScooterDto findById(Long id);

    ScooterDto deleteById(Long id);

    ScooterDto updateById(Long id, @Valid ScooterDto scooterDto);

    ScooterDto create(@Valid ScooterDto scooterDto);

    Integer findDistanceTravelledByScooterId(Long id);

    List<RentDto> findSortedByCreationScooterRents(Long id);

    List<ScooterDto> findActiveRentScooters();
}
