package com.senla.training.yeutukhovich.scooterrental.service.review;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public interface ReviewService {

    List<ReviewDto> findAll();

    ReviewDto findById(Long id);

    ReviewDto deleteById(Long id);

    ReviewDto updateById(Long id, @Valid ReviewDto reviewDto);

    ReviewDto create(@Valid ReviewDto reviewDto);

    BigDecimal findAverageScore();
}
