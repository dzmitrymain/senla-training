package com.senla.training.yeutukhovich.scooterrental.service.review;

import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;

import java.util.List;

public interface ReviewService {

    List<ReviewDto> findAll();


    ReviewDto findById(Long id);

    ReviewDto deleteById(Long id);

    ReviewDto updateById(Long id, ReviewDto reviewDto);

    ReviewDto create(ReviewDto reviewDto);

}
