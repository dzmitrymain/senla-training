package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScooterDtoMapper {

    private final ModelDtoMapper modelDtoMapper;
    private final SpotDtoMapper spotDtoMapper;

    @Autowired
    public ScooterDtoMapper(ModelDtoMapper modelDtoMapper, SpotDtoMapper spotDtoMapper) {
        this.modelDtoMapper = modelDtoMapper;
        this.spotDtoMapper = spotDtoMapper;
    }

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

    public Scooter map(ScooterDto scooterDto) {
        if (scooterDto == null) {
            return null;
        }
        Scooter scooter = new Scooter();
        scooter.setId(scooterDto.getId());
        scooter.setModel(modelDtoMapper.map(scooterDto.getModelDto()));
        scooter.setSpot(spotDtoMapper.map(scooterDto.getSpotDto()));
        scooter.setBeginOperationDate(scooterDto.getBeginOperationDate());
        return scooter;
    }

}
