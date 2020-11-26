package com.senla.training.yeutukhovich.scooterrental.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReviewDtoMapper {

    private final ProfileDtoMapper profileDtoMapper;
    private final ModelDtoMapper modelDtoMapper;

    @Autowired
    public ReviewDtoMapper(ProfileDtoMapper profileDtoMapper, ModelDtoMapper modelDtoMapper) {
        this.profileDtoMapper = profileDtoMapper;
        this.modelDtoMapper = modelDtoMapper;
    }

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

    public Review map(ReviewDto reviewDto) {
        if (reviewDto == null) {
            return null;
        }
        Review review = new Review();
        review.setId(reviewDto.getId());
        review.setProfile(profileDtoMapper.map(reviewDto.getProfileDto()));
        review.setModel(modelDtoMapper.map(reviewDto.getModelDto()));
        review.setScore(reviewDto.getScore());
        review.setOpinion(reviewDto.getOpinion());
        review.setCreationDate(reviewDto.getCreationDate());
        return review;
    }
}
