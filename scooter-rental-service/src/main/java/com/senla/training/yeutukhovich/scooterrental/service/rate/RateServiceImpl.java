package com.senla.training.yeutukhovich.scooterrental.service.rate;

import com.senla.training.yeutukhovich.scooterrental.dao.rate.RateDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.RateDtoMapper;
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
public class RateServiceImpl implements RateService {

    private static final String ENTITY_NAME = "Rate";

    private final RateDao rateDao;
    private final RateDtoMapper rateDtoMapper;

    @Autowired
    public RateServiceImpl(RateDao rateDao, RateDtoMapper rateDtoMapper) {
        this.rateDao = rateDao;
        this.rateDtoMapper = rateDtoMapper;
    }

    @Override
    @Transactional
    public List<RateDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return rateDao.findAll().stream()
                .map(rateDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RateDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return rateDtoMapper.map(findRatesById(id));
    }

    @Override
    @Transactional
    public RateDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Rate rateToDelete = findRatesById(id);
        rateDao.delete(rateToDelete);
        return rateDtoMapper.map(rateToDelete);
    }

    @Override
    @Transactional
    public RateDto updateById(Long id, RateDto rateDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findRatesById(id);
        if (!id.equals(rateDto.getId())) {
            rateDto.setId(id);
        }
        return rateDtoMapper.map(rateDao.update(rateDtoMapper.map(rateDto)));
    }

    @Override
    @Transactional
    public RateDto create(RateDto rateDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Rate rate = rateDtoMapper.map(rateDto);
        rate.setId(null);
        rateDao.add(rate);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, rate.getId());
        return rateDtoMapper.map(rate);
    }

    @Override
    @Transactional
    public List<RateDto> findAllActualRates() {
        log.info(LoggerConstant.RATES_ACTUAL.getMessage());
        return rateDao.findAllActualRates().stream()
                .map(rateDtoMapper::map)
                .collect(Collectors.toList());
    }

    private Rate findRatesById(Long RateId) {
        return rateDao.findById(RateId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
