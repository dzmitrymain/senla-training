package com.senla.training.yeutukhovich.scooterrental.service.rate;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;

import javax.validation.Valid;
import java.util.List;

public interface RateService {

    List<RateDto> findAll();

    RateDto findById(Long id);

    RateDto deleteById(Long id);

    RateDto updateById(Long id, @Valid RateDto rateDto);

    RateDto create(@Valid RateDto rateDto);

    List<RateDto> findAllActualRates();
}
