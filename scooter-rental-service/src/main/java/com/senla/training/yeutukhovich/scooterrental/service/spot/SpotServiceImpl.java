package com.senla.training.yeutukhovich.scooterrental.service.spot;

import com.senla.training.yeutukhovich.scooterrental.dao.spot.SpotDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.location.LocationService;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.SpotDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.scooterrental.validator.DecimalDegrees;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Tuple;
import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class SpotServiceImpl implements SpotService {

    private static final String ENTITY_NAME = "Spot";

    private final SpotDao spotDao;
    private final LocationService locationService;
    private final ScooterDtoMapper scooterDtoMapper;
    private final GeometryFactory geometryFactory;
    private final SpotDtoMapper spotDtoMapper;

    @Autowired
    public SpotServiceImpl(SpotDao spotDao,
                           LocationService locationService,
                           ScooterDtoMapper scooterDtoMapper,
                           GeometryFactory geometryFactory,
                           SpotDtoMapper spotDtoMapper) {
        this.spotDao = spotDao;
        this.locationService = locationService;
        this.scooterDtoMapper = scooterDtoMapper;
        this.geometryFactory = geometryFactory;
        this.spotDtoMapper = spotDtoMapper;
    }

    @Override
    @Transactional
    public List<SpotDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return spotDao.findAll().stream()
                .map(spotDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SpotDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return spotDtoMapper.map(findSpotById(id));
    }

    @Override
    @Transactional
    public SpotDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Spot spotToDelete = findSpotById(id);
        spotDao.delete(spotToDelete);
        return spotDtoMapper.map(spotToDelete);
    }

    @Override
    @Transactional
    public SpotDto updateById(Long id, @Valid SpotDto spotDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findSpotById(id);
        locationService.findById(spotDto.getLocationDto().getId());
        if (!id.equals(spotDto.getId())) {
            spotDto.setId(id);
        }
        return spotDtoMapper.map(spotDao.update(spotDtoMapper.map(spotDto)));
    }

    @Override
    @Transactional
    public SpotDto create(@Valid SpotDto spotDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        spotDto.setLocationDto(locationService.findById(spotDto.getLocationDto().getId()));
        Spot spot = spotDtoMapper.map(spotDto);
        spot.setId(null);
        spotDao.add(spot);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), spot.getId());
        return spotDtoMapper.map(spot);
    }

    @Override
    @Transactional
    public List<Map<String, Long>> findDistancesToSpots(@DecimalDegrees Double latitude,@DecimalDegrees Double longitude) {
        log.info(LoggerConstant.SPOT_DISTANCES.getMessage(), latitude, longitude);
        List<Tuple> resultTupleList = spotDao.findDistancesFromPointToSpots(
                geometryFactory.createPoint(new Coordinate(longitude, latitude)));

        List<Map<String, Long>> responseList = new ArrayList<>();
        resultTupleList.forEach(tuple -> {
            Map<String, Long> distanceById = new LinkedHashMap<>();
            distanceById.put("spotId", tuple.get("spotId", BigInteger.class).longValue());
            distanceById.put("distanceInMeters", tuple.get("distance", Double.class).longValue());
            responseList.add(distanceById);
        });
        return responseList;
    }

    @Override
    @Transactional
    public List<ScooterDto> findAvailableScootersBySpotId(Long id) {
        log.info(LoggerConstant.SPOT_AVAILABLE_SCOOTERS.getMessage(), id);
        return spotDao.findAvailableScootersBySpotId(id).stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ScooterDto> findSpotScooters(Long id) {
        log.info(LoggerConstant.SPOT_SCOOTERS.getMessage(), id);
        return findSpotById(id).getScooters().stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Spot findSpotById(Long spotId) {
        return spotDao.findById(spotId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
