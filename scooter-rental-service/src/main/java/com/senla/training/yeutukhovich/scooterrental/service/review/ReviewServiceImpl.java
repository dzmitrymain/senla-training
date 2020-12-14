package com.senla.training.yeutukhovich.scooterrental.service.review;

import com.senla.training.yeutukhovich.scooterrental.dao.review.ReviewDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ReviewDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.UserDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.model.ModelService;
import com.senla.training.yeutukhovich.scooterrental.service.profile.ProfileService;
import com.senla.training.yeutukhovich.scooterrental.service.user.UserService;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class ReviewServiceImpl implements ReviewService {

    private static final String ENTITY_NAME = "Review";

    private final ReviewDao reviewDao;
    private final UserService userService;
    private final ModelService modelService;
    private final ProfileService profileService;
    private final UserDtoMapper userDtoMapper;
    private final ReviewDtoMapper reviewDtoMapper;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao,
                             UserService userService,
                             ModelService modelService,
                             ProfileService profileService,
                             UserDtoMapper userDtoMapper,
                             ReviewDtoMapper reviewDtoMapper) {
        this.reviewDao = reviewDao;
        this.userService = userService;
        this.modelService = modelService;
        this.profileService = profileService;
        this.userDtoMapper = userDtoMapper;
        this.reviewDtoMapper = reviewDtoMapper;
    }

    @Override
    @Transactional
    public List<ReviewDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return reviewDao.findAll().stream()
                .map(reviewDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReviewDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return reviewDtoMapper.map(findReviewById(id));
    }

    @Override
    @Transactional
    public ReviewDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Review reviewToDelete = findReviewById(id);
        if (!checkAdmin()) {
            checkUserMatch(reviewToDelete.getProfile().getUser());
        }
        reviewDao.delete(reviewToDelete);
        return reviewDtoMapper.map(reviewToDelete);
    }

    @Override
    @Transactional
    public ReviewDto updateById(Long id, @Valid ReviewDto reviewDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        Review checkedReview = findReviewById(id);
        checkUserMatch(checkedReview.getProfile().getUser());
        reviewDto.setProfileDto(profileService.findById(checkedReview.getProfile().getId()));
        modelService.findById(reviewDto.getModelDto().getId());
        if (!id.equals(reviewDto.getId())) {
            reviewDto.setId(id);
        }
        return reviewDtoMapper.map(reviewDao.update(reviewDtoMapper.map(reviewDto)));
    }

    @Override
    @Transactional
    public ReviewDto create(@Valid ReviewDto reviewDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        reviewDto.setModelDto(modelService.findById(reviewDto.getModelDto().getId()));
        reviewDto.setProfileDto(profileService.findById(reviewDto.getProfileDto().getId()));
        checkUserMatch(userDtoMapper.map(userService.findById(reviewDto.getProfileDto().getUserId())));
        Review review = reviewDtoMapper.map(reviewDto);
        review.setId(null);
        reviewDao.add(review);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, review.getId());
        return reviewDtoMapper.map(review);
    }

    @Override
    @Transactional
    public BigDecimal findAverageScore() {
        log.info(LoggerConstant.REVIEWS_SCORE.getMessage());
        return BigDecimal.valueOf(reviewDao.findAverageScore()).setScale(1, RoundingMode.HALF_UP);
    }

    private Review findReviewById(Long reviewId) {
        return reviewDao.findById(reviewId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }

    private void checkUserMatch(User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !user.getUsername().equals(auth.getName())) {
            throw new AccessDeniedException(ExceptionConstant.USER_ACCESS_DENIED.getMessage());
        }
    }

    private boolean checkAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails == null) {
            return false;
        }
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return true;
            }
        }
        return false;
    }
}
