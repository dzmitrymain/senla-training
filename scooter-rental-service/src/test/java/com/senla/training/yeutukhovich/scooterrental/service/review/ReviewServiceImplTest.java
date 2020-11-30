package com.senla.training.yeutukhovich.scooterrental.service.review;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.profile.ProfileDao;
import com.senla.training.yeutukhovich.scooterrental.dao.review.ReviewDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ModelDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ReviewDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ReviewServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Review REVIEW = Mockito.mock(Review.class);
    private static final ReviewDto REVIEW_DTO = Mockito.mock(ReviewDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);
    private static final Profile PROFILE = Mockito.mock(Profile.class);
    private static final ProfileDto PROFILE_DTO = Mockito.mock(ProfileDto.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewDao reviewDao;
    @Autowired
    private ProfileDao profileDao;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private ReviewDtoMapper reviewDtoMapper;
    @Autowired
    private ProfileDtoMapper profileDtoMapper;
    @Autowired
    private ModelDtoMapper modelDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(REVIEW.getId()).thenReturn(ENTITY_ID);
        Mockito.when(REVIEW_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(REVIEW.getProfile()).thenReturn(PROFILE);
        Mockito.when(PROFILE.getId()).thenReturn(ENTITY_ID);
        Mockito.when(REVIEW_DTO.getModelDto()).thenReturn(MODEL_DTO);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(REVIEW_DTO.getProfileDto()).thenReturn(PROFILE_DTO);
        Mockito.when(PROFILE_DTO.getId()).thenReturn(ENTITY_ID);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(reviewDtoMapper.map(REVIEW)).thenReturn(REVIEW_DTO);
        Mockito.when(reviewDtoMapper.map(REVIEW_DTO)).thenReturn(REVIEW);
        Mockito.when(profileDtoMapper.map(PROFILE)).thenReturn(PROFILE_DTO);
        Mockito.when(modelDtoMapper.map(MODEL)).thenReturn(MODEL_DTO);
        Mockito.clearInvocations(reviewDao, modelDao, profileDao);
    }

    @Test
    void ReviewServiceImpl_findAverageScore() {
        Mockito.when(reviewDao.findAverageScore()).thenReturn(Double.MIN_NORMAL);

        reviewService.findAverageScore();

        Mockito.verify(reviewDao, Mockito.times(1)).findAverageScore();
    }

    @Test
    void ReviewServiceImpl_findAll() {
        reviewService.findAll();

        Mockito.verify(reviewDao, Mockito.times(1)).findAll();
    }

    @Test
    void ReviewServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(REVIEW));

        ReviewDto reviewDto = reviewService.findById(ENTITY_ID);

        Assertions.assertNotNull(reviewDto);
    }

    @Test
    void ReviewServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> reviewService.findById(ENTITY_ID));
    }

    @Test
    void ReviewServiceImpl_deleteById() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(REVIEW));

        ReviewDto reviewDto = reviewService.deleteById(ENTITY_ID);

        Mockito.verify(reviewDao, Mockito.times(1)).delete(REVIEW);
        Assertions.assertNotNull(reviewDto);
    }

    @Test
    void ReviewServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> reviewService.deleteById(ENTITY_ID));
    }

    @Test
    void ReviewServiceImpl_updateById() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(REVIEW));
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(reviewDao.update(REVIEW)).thenReturn(REVIEW);
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));

        ReviewDto reviewDto = reviewService.updateById(ENTITY_ID, REVIEW_DTO);

        Mockito.verify(reviewDao, Mockito.times(1)).update(REVIEW);
        Assertions.assertNotNull(reviewDto);
    }

    @Test
    void ReviewServiceImpl_updateById_shouldThrowBusinessExceptionIfReviewNotExist() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> reviewService.updateById(ENTITY_ID, REVIEW_DTO));
    }

    @Test
    void ReviewServiceImpl_updateById_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(REVIEW));
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> reviewService.updateById(ENTITY_ID, REVIEW_DTO));
    }

    @Test
    void ReviewServiceImpl_updateById_shouldThrowBusinessExceptionIfProfileNotExist() {
        Mockito.when(reviewDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(REVIEW));
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        Assertions.assertThrows(BusinessException.class, () -> reviewService.updateById(ENTITY_ID, REVIEW_DTO));
    }

    @Test
    void ReviewServiceImpl_create() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));

        ReviewDto reviewDto = reviewService.create(REVIEW_DTO);

        Mockito.verify(reviewDao, Mockito.times(1)).add(REVIEW);
        Assertions.assertNotNull(reviewDto);
    }

    @Test
    void ReviewServiceImpl_create_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(PROFILE));

        Assertions.assertThrows(BusinessException.class, () -> reviewService.create(REVIEW_DTO));
    }

    @Test
    void ReviewServiceImpl_create_shouldThrowBusinessExceptionIfProfileNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(profileDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> reviewService.create(REVIEW_DTO));
    }
}
