package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewDtoMapper {

    @Autowired
    private ProfileDtoMapper profileDtoMapper;
    @Autowired
    ModelDtoMapper modelDtoMapper;

    public ReviewDto map(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewDto(
                review.getId(),
                profileDtoMapper.map(review.getProfile()),
                modelDtoMapper.map(review.getModel()),
                review.getScore(),
                review.getOpinion(),
                review.getCreationDate());
    }
}
