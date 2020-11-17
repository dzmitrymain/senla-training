package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RateDtoMapper {

    @Autowired
    private ModelDtoMapper modelDtoMapper;

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
}
