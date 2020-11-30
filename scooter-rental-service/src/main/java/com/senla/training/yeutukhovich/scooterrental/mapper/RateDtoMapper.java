package com.senla.training.yeutukhovich.scooterrental.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateDtoMapper {

    private final ModelDtoMapper modelDtoMapper;

    @Autowired
    public RateDtoMapper(ModelDtoMapper modelDtoMapper) {
        this.modelDtoMapper = modelDtoMapper;
    }

    public RateDto map(Rate rate) {
        if (rate == null) {
            return null;
        }
        return new RateDto(
                rate.getId(),
                modelDtoMapper.map(rate.getModel()),
                rate.getPerHour(),
                rate.getWeekendPerHour(),
                rate.getCreationDate()
        );
    }

    public Rate map(RateDto rateDto) {
        if (rateDto == null) {
            return null;
        }
        Rate rate = new Rate();
        rate.setId(rateDto.getId());
        rate.setModel(modelDtoMapper.map(rateDto.getModelDto()));
        rate.setPerHour(rateDto.getPerHour());
        rate.setWeekendPerHour(rateDto.getWeekendPerHour());
        rate.setCreationDate(rateDto.getCreationDate());
        return rate;
    }
}
