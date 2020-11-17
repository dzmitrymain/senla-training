package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.dto.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;

import java.util.List;

public interface ModelService {

    List<ModelDto> findAll();

    ModelDto findById(Long id);

    ModelDto deleteById(Long id);

    ModelDto updateModel(Long id, ModelDto modelDto);

    ModelDto createModel(ModelDto modelDto);

    List<ScooterDto> findModelScooters(Long id);

    List<ReviewDto> findModelReviews(Long id);

    RateDto findCurrentModelRate(Long id);
}
