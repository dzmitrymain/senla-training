package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScooterDtoMapper {

    @Autowired
    private ModelDtoMapper modelDtoMapper;
    @Autowired
    private SpotDtoMapper spotDtoMapper;

    public ScooterDto map(Scooter scooter) {
        if (scooter == null) {
            return null;
        }
        return new ScooterDto(
                scooter.getId(),
                modelDtoMapper.map(scooter.getModel()),
                spotDtoMapper.map(scooter.getSpot()),
                scooter.getBeginOperationDate());
    }

}
