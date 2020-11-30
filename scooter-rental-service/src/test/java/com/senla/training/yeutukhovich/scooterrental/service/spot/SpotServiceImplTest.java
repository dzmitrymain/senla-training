package com.senla.training.yeutukhovich.scooterrental.service.spot;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.location.LocationDao;
import com.senla.training.yeutukhovich.scooterrental.dao.spot.SpotDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Location;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.SpotDtoMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class SpotServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Spot SPOT = Mockito.mock(Spot.class);
    private static final SpotDto SPOT_DTO = Mockito.mock(SpotDto.class);
    private static final Location LOCATION = Mockito.mock(Location.class);
    private static final LocationDto LOCATION_DTO = Mockito.mock(LocationDto.class);

    @Autowired
    private SpotService spotService;

    @Autowired
    private SpotDao spotDao;
    @Autowired
    private LocationDao locationDao;
    @Autowired
    private SpotDtoMapper spotDtoMapper;
    @Autowired
    private GeometryFactory geometryFactory;

    @BeforeAll
    static void setup() {
        Mockito.when(LOCATION.getId()).thenReturn(ENTITY_ID);
        Mockito.when(LOCATION_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(SPOT.getId()).thenReturn(ENTITY_ID);
        Mockito.when(SPOT_DTO.getId()).thenReturn(ENTITY_ID);
        Mockito.when(SPOT_DTO.getLocationDto()).thenReturn(LOCATION_DTO);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(spotDtoMapper.map(SPOT)).thenReturn(SPOT_DTO);
        Mockito.when(spotDtoMapper.map(SPOT_DTO)).thenReturn(SPOT);
        Mockito.clearInvocations(spotDao);
    }

    @Test
    void SpotServiceImpl_findDistancesToSpots() {
        Mockito.when(geometryFactory.createPoint(Mockito.any(Coordinate.class))).thenReturn(Mockito.mock(Point.class));

        spotService.findDistancesToSpots(0.0, 0.0);

        Mockito.verify(spotDao, Mockito.times(1))
                .findDistancesFromPointToSpots(Mockito.any(Point.class));
    }

    @Test
    void SpotServiceImpl_findAvailableScootersBySpotId() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        spotService.findAvailableScootersBySpotId(ENTITY_ID);

        Mockito.verify(spotDao, Mockito.times(1)).findAvailableScootersBySpotId(ENTITY_ID);
    }

    @Test
    void SpotServiceImpl_findAvailableScootersBySpotId_shouldTrowBusinessException() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.findAvailableScootersBySpotId(ENTITY_ID));
    }

    @Test
    void SpotServiceImpl_findSpotScooters() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        spotService.findSpotScooters(ENTITY_ID);

        Mockito.verify(spotDao, Mockito.times(1)).findById(ENTITY_ID);
    }

    @Test
    void SpotServiceImpl_findSpotScooters_shouldThrowBusinessException() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.findSpotScooters(ENTITY_ID));
    }


    @Test
    void SpotServiceImpl_findAll() {
        spotService.findAll();

        Mockito.verify(spotDao, Mockito.times(1)).findAll();
    }

    @Test
    void SpotServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        SpotDto spotDto = spotService.findById(ENTITY_ID);

        Assertions.assertNotNull(spotDto);
    }

    @Test
    void SpotServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.findById(ENTITY_ID));
    }

    @Test
    void SpotServiceImpl_deleteById() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));

        SpotDto spotDto = spotService.deleteById(ENTITY_ID);

        Mockito.verify(spotDao, Mockito.times(1)).delete(SPOT);
        Assertions.assertNotNull(spotDto);
    }

    @Test
    void SpotServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.deleteById(ENTITY_ID));
    }

    @Test
    void SpotServiceImpl_updateById() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));
        Mockito.when(spotDao.update(SPOT)).thenReturn(SPOT);
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));

        SpotDto spotDto = spotService.updateById(ENTITY_ID, SPOT_DTO);

        Mockito.verify(spotDao, Mockito.times(1)).update(SPOT);
        Assertions.assertNotNull(spotDto);
    }

    @Test
    void SpotServiceImpl_updateById_shouldThrowBusinessExceptionIfSpotNotExist() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.updateById(ENTITY_ID, SPOT_DTO));
    }

    @Test
    void SpotServiceImpl_updateById_shouldThrowBusinessExceptionIfLocationNotExist() {
        Mockito.when(spotDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(SPOT));
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.updateById(ENTITY_ID, SPOT_DTO));
    }

    @Test
    void SpotServiceImpl_create() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));


        SpotDto spotDto = spotService.create(SPOT_DTO);

        Mockito.verify(spotDao, Mockito.times(1)).add(SPOT);
        Assertions.assertNotNull(spotDto);
    }

    @Test
    void SpotServiceImpl_create_shouldThrowBusinessException() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> spotService.create(SPOT_DTO));
    }
}