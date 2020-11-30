package com.senla.training.yeutukhovich.scooterrental.service.location;

import com.senla.training.yeutukhovich.scooterrental.config.TestConfig;
import com.senla.training.yeutukhovich.scooterrental.dao.location.LocationDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Location;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.LocationDtoMapper;
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
class LocationServiceImplTest {

    private static final Long ENTITY_ID = 1L;
    private static final Location LOCATION = Mockito.mock(Location.class);
    private static final LocationDto LOCATION_DTO = Mockito.mock(LocationDto.class);

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationDao locationDao;
    @Autowired
    private LocationDtoMapper locationDtoMapper;

    @BeforeAll
    static void setup() {
        Mockito.when(LOCATION.getId()).thenReturn(ENTITY_ID);
        Mockito.when(LOCATION_DTO.getId()).thenReturn(ENTITY_ID);
    }

    @BeforeEach
    void setUp() {
        Mockito.when(locationDtoMapper.map(LOCATION)).thenReturn(LOCATION_DTO);
        Mockito.when(locationDtoMapper.map(LOCATION_DTO)).thenReturn(LOCATION);
        Mockito.clearInvocations(locationDao);
    }

    @Test
    void LocationServiceImpl_findSortedAllLocationsByName() {
        locationService.findSortedAllLocationsByName();

        Mockito.verify(locationDao, Mockito.times(1)).findSortedAllLocationsByName();
    }

    @Test
    void LocationServiceImpl_findLocationProfiles() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));

        locationService.findLocationProfiles(ENTITY_ID);

        Mockito.verify(locationDao, Mockito.times(1)).findById(ENTITY_ID);
    }

    @Test
    void LocationServiceImpl_findLocationProfiles_shouldThrowBusinessException() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> locationService.findLocationProfiles(ENTITY_ID));
    }

    @Test
    void LocationServiceImpl_findLocationSpots() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));

        locationService.findLocationSpots(ENTITY_ID);

        Mockito.verify(locationDao, Mockito.times(1)).findById(ENTITY_ID);
    }

    @Test
    void LocationServiceImpl_findLocationSpots_shouldThrowBusinessException() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> locationService.findLocationSpots(ENTITY_ID));
    }


    @Test
    void LocationServiceImpl_findById_shouldReturnNotNull() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));

        LocationDto locationDto = locationService.findById(ENTITY_ID);

        Assertions.assertNotNull(locationDto);
    }

    @Test
    void LocationServiceImpl_findById_shouldThrowBusinessException() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> locationService.findById(ENTITY_ID));
    }

    @Test
    void LocationServiceImpl_deleteById() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));

        LocationDto locationDto = locationService.deleteById(ENTITY_ID);

        Mockito.verify(locationDao, Mockito.times(1)).delete(LOCATION);
        Assertions.assertNotNull(locationDto);
    }

    @Test
    void LocationServiceImpl_deleteById_shouldThrowBusinessException() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> locationService.deleteById(ENTITY_ID));
    }

    @Test
    void LocationServiceImpl_updateById() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.ofNullable(LOCATION));
        Mockito.when(locationDao.update(LOCATION)).thenReturn(LOCATION);

        LocationDto locationDto = locationService.updateById(ENTITY_ID, LOCATION_DTO);

        Mockito.verify(locationDao, Mockito.times(1)).update(LOCATION);
        Assertions.assertNotNull(locationDto);
    }

    @Test
    void LocationServiceImpl_updateById_shouldThrowBusinessExceptionIfDiscountNotExist() {
        Mockito.when(locationDao.findById(ENTITY_ID)).thenReturn(Optional.empty());

        Assertions.assertThrows(BusinessException.class, () -> locationService.updateById(ENTITY_ID, LOCATION_DTO));
    }

    @Test
    void LocationServiceImpl_create() {
        LocationDto locationDto = locationService.create(LOCATION_DTO);

        Mockito.verify(locationDao, Mockito.times(1)).add(LOCATION);
        Assertions.assertNotNull(locationDto);
    }
}
