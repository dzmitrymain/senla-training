package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;

import javax.validation.Valid;
import java.util.List;

public interface ModelService {

    List<ModelDto> findAll();

    ModelDto findById(Long id);

    ModelDto deleteById(Long id);

    ModelDto updateById(Long id, @Valid ModelDto modelDto);

    ModelDto create(@Valid ModelDto modelDto);

    List<ScooterDto> findModelScooters(Long id);

    List<ReviewDto> findModelReviews(Long id);

    RateDto findCurrentModelRate(Long id);

    DiscountDto findCurrentModelDiscount(Long id);
}
