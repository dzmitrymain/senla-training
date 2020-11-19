package com.senla.training.yeutukhovich.scooterrental.service.scooter;

import com.senla.training.yeutukhovich.scooterrental.dao.scooter.ScooterDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.RentDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScooterServiceImpl implements ScooterService {

    private static final String ENTITY_NAME = "Scooter";

    private final ScooterDao scooterDao;
    private final ScooterDtoMapper scooterDtoMapper;
    private final RentDtoMapper rentDtoMapper;


    @Autowired
    public ScooterServiceImpl(ScooterDao scooterDao, ScooterDtoMapper scooterDtoMapper, RentDtoMapper rentDtoMapper) {
        this.scooterDao = scooterDao;
        this.scooterDtoMapper = scooterDtoMapper;
        this.rentDtoMapper = rentDtoMapper;
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
    public ScooterDto updateById(Long id, ScooterDto scooterDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findScooterById(id);
        if (!id.equals(scooterDto.getId())) {
            scooterDto.setId(id);
        }
        return scooterDtoMapper.map(scooterDao.update(scooterDtoMapper.map(scooterDto)));
    }

    @Override
    @Transactional
    public ScooterDto create(ScooterDto scooterDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Scooter scooter = scooterDtoMapper.map(scooterDto);
        scooter.setId(null);
        scooterDao.add(scooter);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), scooter.getId());
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
        return scooterDao.findSortedByCreationScooterRents(id).stream()
                .map(rentDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Scooter findScooterById(Long scooterId) {
        return scooterDao.findById(scooterId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
