package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.DiscountDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ModelDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.RateDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class ModelServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);
    private static final Rate RATE = Mockito.mock(Rate.class);
    private static final RateDto RATE_DTO = Mockito.mock(RateDto.class);
    private static final Discount DISCOUNT = Mockito.mock(Discount.class);
    private static final DiscountDto DISCOUNT_DTO = Mockito.mock(DiscountDto.class);

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelDao modelDao;
    @Autowired
    private ModelDtoMapper modelDtoMapper;
    @Autowired
    private RateDtoMapper rateDtoMapper;
    @Autowired
    private DiscountDtoMapper discountDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(MODEL.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(modelDtoMapper.map(MODEL)).thenReturn(MODEL_DTO);
        Mockito.when(modelDtoMapper.map(MODEL_DTO)).thenReturn(MODEL);
        Mockito.when((rateDtoMapper.map(RATE))).thenReturn(RATE_DTO);
        Mockito.when(discountDtoMapper.map(DISCOUNT)).thenReturn(DISCOUNT_DTO);
        Mockito.clearInvocations(modelDao);
    }

    @Test
    void ModelServiceImpl_findModelScooters() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        modelService.findModelScooters(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).findById(ENTITY_ID);
    }

    @Test
    void ModelServiceImpl_findModelScooters_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findModelScooters(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findModelReviews() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        modelService.findModelReviews(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).findById(ENTITY_ID);
    }

    @Test
    void ModelServiceImpl_findModelReviews_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findModelReviews(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelRate() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));

        RateDto rateDto = modelService.findCurrentModelRate(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).findById(ENTITY_ID);
        Mockito.verify(modelDao, Mockito.times(1)).findCurrentRateByModelId(ENTITY_ID);
        Assertions.assertNotNull(rateDto);
    }

    @Test
    void ModelServiceImpl_findCurrentModelRate_throwBusinessExceptionIfModelNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelRate(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelRate_throwBusinessExceptionIfRateNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelRate(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelDiscount() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentDiscountByModelId(ENTITY_ID)).thenReturn(Optional.ofNullable(DISCOUNT));

        DiscountDto discountDto = modelService.findCurrentModelDiscount(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).findById(ENTITY_ID);
        Mockito.verify(modelDao, Mockito.times(1)).findCurrentDiscountByModelId(ENTITY_ID);
        Assertions.assertNotNull(discountDto);
    }

    @Test
    void ModelServiceImpl_findCurrentModelDiscount_throwBusinessExceptionIfModelNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelDiscount(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelDiscount_throwBusinessExceptionIfDiscountNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentDiscountByModelId(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelDiscount(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelPrice() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));
        Mockito.when(modelDao.findCurrentDiscountByModelId(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(RATE_DTO.getPerHour()).thenReturn(BigDecimal.ONE);
        Mockito.when(RATE_DTO.getWeekendPerHour()).thenReturn(BigDecimal.ONE);

        BigDecimal price = modelService.findCurrentModelPrice(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).findById(ENTITY_ID);
        Mockito.verify(modelDao, Mockito.times(1)).findCurrentRateByModelId(ENTITY_ID);
        Mockito.verify(modelDao, Mockito.times(1)).findCurrentDiscountByModelId(ENTITY_ID);
        Assertions.assertNotNull(price);
    }

    @Test
    void ModelServiceImpl_findCurrentModelPrice_throwBusinessExceptionIfModelNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelPrice(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findCurrentModelPrice_throwBusinessExceptionIfRateNotExist() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.findCurrentRateByModelId(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findCurrentModelPrice(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_findAll() {
        modelService.findAll();

        Mockito.verify(modelDao, Mockito.times(1)).findAll();
    }

    @Test
    void ModelServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        ModelDto modelDto = modelService.findById(ENTITY_ID);

        Assertions.assertNotNull(modelDto);
    }

    @Test
    void ModelServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.findById(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_deleteById() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        ModelDto modelDto = modelService.deleteById(ENTITY_ID);

        Mockito.verify(modelDao, Mockito.times(1)).delete(MODEL);
        Assertions.assertNotNull(modelDto);
    }

    @Test
    void ModelServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.deleteById(ENTITY_ID));
    }

    @Test
    void ModelServiceImpl_updateById() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(modelDao.update(MODEL)).thenReturn(MODEL);

        ModelDto modelDto = modelService.updateById(ENTITY_ID, MODEL_DTO);

        Mockito.verify(modelDao, Mockito.times(1)).update(MODEL);
        Assertions.assertNotNull(modelDto);
    }

    @Test
    void ModelServiceImpl_updateById_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> modelService.updateById(ENTITY_ID, MODEL_DTO));
    }

    @Test
    void ModelServiceImpl_create() {
        ModelDto modelDto = modelService.create(MODEL_DTO);

        Mockito.verify(modelDao, Mockito.times(1)).add(MODEL);
        Assertions.assertNotNull(modelDto);
    }
}
