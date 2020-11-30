package com.senla.training.yeutukhovich.scooterrental.service.discount;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.discount.DiscountDao;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.DiscountDtoMapper;
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
class DiscountServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Discount DISCOUNT = Mockito.mock(Discount.class);
    private static final DiscountDto DISCOUNT_DTO = Mockito.mock(DiscountDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);

    @Autowired
    private DiscountService discountService;

    @Autowired
    private DiscountDao discountDao;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private DiscountDtoMapper discountDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(DISCOUNT.getId()).thenReturn(ENTITY_ID);
        Mockito.when(DISCOUNT_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(DISCOUNT_DTO.getModelDto()).thenReturn(MODEL_DTO);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(discountDtoMapper.map(DISCOUNT)).thenReturn(DISCOUNT_DTO);
        Mockito.when(discountDtoMapper.map(DISCOUNT_DTO)).thenReturn(DISCOUNT);
        Mockito.clearInvocations(discountDao);
    }


    @Test
    void DiscountServiceImpl_findAllActiveDiscounts() {
        discountService.findAllActiveDiscounts();
        Mockito.verify(discountDao, Mockito.times(1)).findAllActiveDiscounts();
    }

    @Test
    void DiscountServiceImpl_findAll() {
        discountService.findAll();

        Mockito.verify(discountDao, Mockito.times(1)).findAll();
    }

    @Test
    void DiscountServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(DISCOUNT));

        DiscountDto discountDtoResult = discountService.findById(ENTITY_ID);

        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void DiscountServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> discountService.findById(ENTITY_ID));
    }

    @Test
    void DiscountServiceImpl_deleteById() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(DISCOUNT));

        DiscountDto discountDtoResult = discountService.deleteById(ENTITY_ID);

        Mockito.verify(discountDao, Mockito.times(1)).delete(DISCOUNT);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void DiscountServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> discountService.deleteById(ENTITY_ID));
    }

    @Test
    void DiscountServiceImpl_updateById() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(DISCOUNT));
        Mockito.when(discountDao.update(DISCOUNT)).thenReturn(DISCOUNT);
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        DiscountDto discountDtoResult = discountService.updateById(ENTITY_ID, DISCOUNT_DTO);

        Mockito.verify(discountDao, Mockito.times(1)).update(DISCOUNT);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void DiscountServiceImpl_updateById_shouldThrowBusinessExceptionIfDiscountNotExist() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> discountService.updateById(ENTITY_ID, DISCOUNT_DTO));
    }

    @Test
    void DiscountServiceImpl_updateById_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(discountDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(DISCOUNT));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> discountService.updateById(ENTITY_ID, DISCOUNT_DTO));
    }

    @Test
    void DiscountServiceImpl_create() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        DiscountDto discountDtoResult = discountService.create(DISCOUNT_DTO);

        Mockito.verify(discountDao, Mockito.times(1)).add(DISCOUNT);
        Assertions.assertNotNull(discountDtoResult);
    }

    @Test
    void DiscountServiceImpl_create_shouldThrowBusinessException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> discountService.create(DISCOUNT_DTO));
    }
}