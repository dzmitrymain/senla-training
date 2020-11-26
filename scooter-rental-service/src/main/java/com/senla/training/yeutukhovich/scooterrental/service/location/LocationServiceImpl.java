package com.senla.training.yeutukhovich.scooterrental.service.location;

import com.senla.training.yeutukhovich.scooterrental.dao.location.LocationDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Location;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.LocationDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ProfileDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.SpotDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class LocationServiceImpl implements LocationService {

    private static final String ENTITY_NAME = "Location";

    private final LocationDao locationDao;
    private final LocationDtoMapper locationDtoMapper;
    private final ProfileDtoMapper profileDtoMapper;
    private final SpotDtoMapper spotDtoMapper;

    @Autowired
    public LocationServiceImpl(LocationDao locationDao,
                               LocationDtoMapper locationDtoMapper,
                               ProfileDtoMapper profileDtoMapper,
                               SpotDtoMapper spotDtoMapper) {
        this.locationDao = locationDao;
        this.locationDtoMapper = locationDtoMapper;
        this.profileDtoMapper = profileDtoMapper;
        this.spotDtoMapper = spotDtoMapper;
    }

    @Override
    @Transactional
    public List<LocationDto> findSortedAllLocationsByName() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return locationDao.findSortedAllLocationsByName().stream()
                .map(locationDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LocationDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return locationDtoMapper.map(findLocationById(id));
    }

    @Override
    @Transactional
    public LocationDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Location locationToDelete = findLocationById(id);
        locationDao.delete(locationToDelete);
        return locationDtoMapper.map(locationToDelete);
    }

    @Override
    @Transactional
    public LocationDto updateById(Long id, @Valid LocationDto locationDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findLocationById(id);
        if (!id.equals(locationDto.getId())) {
            locationDto.setId(id);
        }
        return locationDtoMapper.map(locationDao.update(locationDtoMapper.map(locationDto)));
    }

    @Override
    @Transactional
    public LocationDto create(@Valid LocationDto locationDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Location location = locationDtoMapper.map(locationDto);
        location.setId(null);
        locationDao.add(location);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, location.getId());
        return locationDtoMapper.map(location);
    }


    @Override
    @Transactional
    public List<ProfileDto> findLocationProfiles(Long id) {
        log.info(LoggerConstant.LOCATION_PROFILES.getMessage(), id);
        return findLocationById(id).getProfiles().stream()
                .map(profileDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<SpotDto> findLocationSpots(Long id) {
        log.info(LoggerConstant.LOCATION_SPOTS.getMessage(), id);
        return findLocationById(id).getSpots().stream()
                .map(spotDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Location findLocationById(Long locationId) {
        return locationDao.findById(locationId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
