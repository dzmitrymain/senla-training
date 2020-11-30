package com.senla.training.yeutukhovich.scooterrental.service.scooter;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.dao.spot.SpotDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.ScooterDtoMapper;
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
class ScooterServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Scooter SCOOTER = Mockito.mock(Scooter.class);
    private static final ScooterDto SCOOTER_DTO = Mockito.mock(ScooterDto.class);
    private static final Model MODEL = Mockito.mock(Model.class);
    private static final ModelDto MODEL_DTO = Mockito.mock(ModelDto.class);
    private static final Spot SPOT = Mockito.mock(Spot.class);
    private static final SpotDto SPOT_DTO = Mockito.mock(SpotDto.class);

    @Autowired
    private ScooterService scooterService;
    @Autowired
    private ScooterDao scooterDao;
    @Autowired
    private ModelDao modelDao;
    @Autowired
    private SpotDao spotDao;
    @Autowired
    private ScooterDtoMapper scooterDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(SCOOTER.getId()).thenReturn(ENTITY_ID);
        Mockito.when(SCOOTER_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL.getId()).thenReturn(ENTITY_ID);
        Mockito.when(MODEL_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(SCOOTER_DTO.getModelDto()).thenReturn(MODEL_DTO);
        Mockito.when(SCOOTER_DTO.getSpotDto()).thenReturn(SPOT_DTO);
        Mockito.when(SPOT_DTO.getId()).thenReturn(ENTITY_ID);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(scooterDtoMapper.map(SCOOTER)).thenReturn(SCOOTER_DTO);
        Mockito.when(scooterDtoMapper.map(SCOOTER_DTO)).thenReturn(SCOOTER);
        Mockito.clearInvocations(scooterDao);
    }

    @Test
    void ScooterServiceImpl_findDistanceTravelledByScooterId() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));

        scooterService.findDistanceTravelledByScooterId(ENTITY_ID);

        Mockito.verify(scooterDao, Mockito.times(1)).findDistanceTravelledByScooterId(ENTITY_ID);
    }

    @Test
    void ScooterServiceImpl_findDistanceTravelledByScooterId_shouldThrowBusinessException() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.findDistanceTravelledByScooterId(ENTITY_ID));
    }


    @Test
    void ScooterServiceImpl_findSortedByCreationScooterRents() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));

        scooterService.findSortedByCreationScooterRents(ENTITY_ID);

        Mockito.verify(scooterDao, Mockito.times(1)).findSortedByCreationScooterRents(ENTITY_ID);
    }

    @Test
    void ScooterServiceImpl_findSortedByCreationScooterRents_shouldThrowBusinessException() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.findSortedByCreationScooterRents(ENTITY_ID));
    }

    @Test
    void ScooterServiceImpl_findActiveRentScooters() {
        scooterService.findActiveRentScooters();

        Mockito.verify(scooterDao, Mockito.times(1)).findActiveRentScooters();
    }

    @Test
    void ScooterServiceImpl_findAll() {
        scooterService.findAll();

        Mockito.verify(scooterDao, Mockito.times(1)).findAll();
    }

    @Test
    void ScooterServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));

        ScooterDto scooterDto = scooterService.findById(ENTITY_ID);

        Assertions.assertNotNull(scooterDto);
    }

    @Test
    void ScooterServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.findById(ENTITY_ID));
    }

    @Test
    void ScooterServiceImpl_deleteById() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));

        ScooterDto scooterDto = scooterService.deleteById(ENTITY_ID);

        Mockito.verify(scooterDao, Mockito.times(1)).delete(SCOOTER);
        Assertions.assertNotNull(scooterDto);
    }

    @Test
    void ScooterServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.deleteById(ENTITY_ID));
    }

    @Test
    void ScooterServiceImpl_updateById() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));
        Mockito.when(scooterDao.update(SCOOTER)).thenReturn(SCOOTER);
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        ScooterDto scooterDto = scooterService.updateById(ENTITY_ID, SCOOTER_DTO);

        Mockito.verify(scooterDao, Mockito.times(1)).update(SCOOTER);
        Assertions.assertNotNull(scooterDto);
    }

    @Test
    void ScooterServiceImpl_updateById_shouldThrowBusinessExceptionIfScooterNotExist() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.updateById(ENTITY_ID, SCOOTER_DTO));
    }

    @Test
    void ScooterServiceImpl_updateById_shouldThrowBusinessExceptionIfModelNotExist() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.updateById(ENTITY_ID, SCOOTER_DTO));
    }

    @Test
    void ScooterServiceImpl_updateById_shouldThrowBusinessExceptionIfSpotNotExist() {
        Mockito.when(scooterDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SCOOTER));
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));

        Assertions.assertThrows(BusinessException.class, () -> scooterService.updateById(ENTITY_ID, SCOOTER_DTO));
    }

    @Test
    void ScooterServiceImpl_create() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        ScooterDto scooterDto = scooterService.create(SCOOTER_DTO);

        Mockito.verify(scooterDao, Mockito.times(1)).add(SCOOTER);
        Assertions.assertNotNull(scooterDto);
    }

    @Test
    void ScooterServiceImpl_create_shouldThrowBusinessIfModelNotExistException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.empty());
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        Assertions.assertThrows(BusinessException.class, () -> scooterService.create(SCOOTER_DTO));
    }

    @Test
    void ScooterServiceImpl_create_shouldThrowBusinessIfSpotNotExistException() {
        Mockito.when(modelDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(MODEL));
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> scooterService.create(SCOOTER_DTO));
    }
}
