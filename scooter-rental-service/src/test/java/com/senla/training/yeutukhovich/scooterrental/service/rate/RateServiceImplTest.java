package com.senla.training.yeutukhovich.scooterrental.service.rate;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.rate.RateDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
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

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class RateServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Rate RATE = Mockito.mock(Rate.class);
    private static final RateDto RATE_DTO = Mockito.mock(RateDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);

    @Autowired
    private RateService rateService;

    @Autowired
    private RateDao rateDao;
    @Autowired
    private RateDtoMapper rateDtoMapper;
    @Autowired
    private ModelDao modelDao;

    @BeforeAll
    static void setup() {
        Mockito.when(RATE.getId()).thenReturn(ENTITY_ID);
        Mockito.when(RATE_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(RATE_DTO.getModelDto()).thenReturn(MODEL_DTO);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(rateDtoMapper.map(RATE)).thenReturn(RATE_DTO);
        Mockito.when(rateDtoMapper.map(RATE_DTO)).thenReturn(RATE);
        Mockito.clearInvocations(rateDao);
    }

    @Test
    void RateServiceImpl_findAllActualRates() {
        rateService.findAllActualRates();
        Mockito.verify(rateDao, Mockito.times(1)).findAllActualRates();
    }

    @Test
    void RateServiceImpl_findAll() {
        rateService.findAll();
        Mockito.verify(rateDao, Mockito.times(1)).findAll();
    }

    @Test
    void RateServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));

        RateDto rateDto = rateService.findById(ENTITY_ID);

        Assertions.assertNotNull(rateDto);
    }

    @Test
    void RateServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rateService.findById(ENTITY_ID));
    }

    @Test
    void RateServiceImpl_deleteById() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));

        RateDto rateDto = rateService.deleteById(ENTITY_ID);

        Mockito.verify(rateDao, Mockito.times(1)).delete(RATE);
        Assertions.assertNotNull(rateDto);
    }

    @Test
    void RateServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rateService.deleteById(ENTITY_ID));
    }

    @Test
    void RateServiceImpl_updateById() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));
        Mockito.when(rateDao.update(RATE)).thenReturn(RATE);
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        RateDto rateDto = rateService.updateById(ENTITY_ID, RATE_DTO);

        Mockito.verify(rateDao, Mockito.times(1)).update(RATE);
        Assertions.assertNotNull(rateDto);
    }

    @Test
    void RateServiceImpl_updateById_shouldThrowBusinessExceptionIfRateNotExist() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rateService.updateById(ENTITY_ID, RATE_DTO));
    }

    @Test
    void RateServiceImpl_updateById_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(rateDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(RATE));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rateService.updateById(ENTITY_ID, RATE_DTO));
    }

    @Test
    void RateServiceImpl_create() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        RateDto discountDtoResult = rateService.create(RATE_DTO);

        Mockito.verify(rateDao, Mockito.times(1)).add(RATE);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void RateServiceImpl_create_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> rateService.create(RATE_DTO));
    }
}