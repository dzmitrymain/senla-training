package com.senla.training.yeutukhovich.scooterrental.service.scooter;

import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.model.ModelService;
import com.senla.training.yeutukhovich.scooterrental.service.spot.SpotService;
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
public class ScooterServiceImpl implements ScooterService {

    private static final String ENTITY_NAME = "Scooter";

    private final ScooterDao scooterDao;
    private final ScooterDtoMapper scooterDtoMapper;
    private final RentDtoMapper rentDtoMapper;
    private final ModelService modelService;
    private final SpotService spotService;


    @Autowired
    public ScooterServiceImpl(ScooterDao scooterDao,
                              ScooterDtoMapper scooterDtoMapper,
                              RentDtoMapper rentDtoMapper,
                              ModelService modelService,
                              SpotService spotService) {
        this.scooterDao = scooterDao;
        this.scooterDtoMapper = scooterDtoMapper;
        this.rentDtoMapper = rentDtoMapper;
        this.modelService = modelService;
        this.spotService = spotService;
    }

    @Override
    @Transactional
    public List<ScooterDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return scooterDao.findAll().stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ScooterDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return scooterDtoMapper.map(findScooterById(id));
    }

    @Override
    @Transactional
    public ScooterDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Scooter scooterToDelete = findScooterById(id);
        scooterDao.delete(scooterToDelete);
        return scooterDtoMapper.map(scooterToDelete);
    }

    @Override
    @Transactional
    public ScooterDto updateById(Long id, @Valid ScooterDto scooterDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findScooterById(id);
        modelService.findById(scooterDto.getModelDto().getId());
        spotService.findById(scooterDto.getSpotDto().getId());
        if (!id.equals(scooterDto.getId())) {
            scooterDto.setId(id);
        }
        return scooterDtoMapper.map(scooterDao.update(scooterDtoMapper.map(scooterDto)));
    }

    @Override
    @Transactional
    public ScooterDto create(@Valid ScooterDto scooterDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        scooterDto.setModelDto(modelService.findById(scooterDto.getModelDto().getId()));
        scooterDto.setSpotDto(spotService.findById(scooterDto.getSpotDto().getId()));
        Scooter scooter = scooterDtoMapper.map(scooterDto);
        scooter.setId(null);
        scooterDao.add(scooter);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, scooter.getId());
        return scooterDtoMapper.map(scooter);
    }

    @Override
    @Transactional
    public Integer findDistanceTravelledByScooterId(Long id) {
        log.info(LoggerConstant.SCOOTER_DISTANCE.getMessage());
        findScooterById(id);
        return scooterDao.findDistanceTravelledByScooterId(id);
    }

    @Override
    @Transactional
    public List<RentDto> findSortedByCreationScooterRents(Long id) {
        log.info(LoggerConstant.SCOOTER_RENTS.getMessage(), id);
        findScooterById(id);
        return scooterDao.findSortedByCreationScooterRents(id).stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ScooterDto> findActiveRentScooters() {
        log.info(LoggerConstant.SCOOTER_ACTIVE_RENTS.getMessage());
        return scooterDao.findActiveRentScooters().stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Scooter findScooterById(Long scooterId) {
        return scooterDao.findById(scooterId).orElseThrow(() -> new BusinessException(
                String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND));
    }
}
