package com.senla.training.yeutukhovich.scooterrental.service.rate;

import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;

import java.util.List;

public interface RateService {

    List<RateDto> findAll();

    RateDto findById(Long id);

    RateDto deleteById(Long id);

    RateDto updateById(Long id, RateDto rateDto);

    RateDto create(RateDto rateDto);

    List<RateDto> findAllActualRates();
}
