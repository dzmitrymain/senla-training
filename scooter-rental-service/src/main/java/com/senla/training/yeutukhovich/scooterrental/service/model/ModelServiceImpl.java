package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.dto.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ModelDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.RateDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ReviewDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.ScooterDtoMapper;
import com.senla.training.yeutukhovich.scooterrental.util.constant.ExceptionConstant;
import com.senla.training.yeutukhovich.scooterrental.util.constant.LoggerConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

    private final ModelDao modelDao;
    private final ModelDtoMapper modelDtoMapper;
    private final ScooterDtoMapper scooterDtoMapper;
    private final ReviewDtoMapper reviewDtoMapper;
    private final RateDtoMapper rateDtoMapper;


    @Autowired
    public ModelServiceImpl(ModelDao modelDao, ModelDtoMapper modelDtoMapper, ScooterDtoMapper scooterDtoMapper, ReviewDtoMapper reviewDtoMapper, RateDtoMapper rateDtoMapper) {
        this.modelDao = modelDao;
        this.modelDtoMapper = modelDtoMapper;
        this.scooterDtoMapper = scooterDtoMapper;
        this.reviewDtoMapper = reviewDtoMapper;
        this.rateDtoMapper = rateDtoMapper;
    }

    @Override
    public List<ModelDto> findAll() {
        log.info(LoggerConstant.MODELS_SEARCHED.getMessage());
        return modelDao.findAll().stream()
                .map(modelDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ModelDto findById(Long id) {
        log.info(LoggerConstant.MODEL_SEARCHED.getMessage(), id);
        return modelDtoMapper.map(modelDao.findById(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    @Override
    public ModelDto deleteById(Long id) {
        log.info(LoggerConstant.MODEL_DELETE.getMessage(), id);
        return modelDtoMapper.map(modelDao.delete(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    @Override
    public ModelDto updateModel(Long id, ModelDto modelDto) {
        log.info(LoggerConstant.MODEL_UPDATE.getMessage(), id);
        if (!id.equals(modelDto.getId())) {
            modelDto.setId(id);
        }
        modelDao.findById(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
        return modelDtoMapper.map(modelDao.update(modelDtoMapper.map(modelDto)));
    }

    @Override
    public ModelDto createModel(ModelDto modelDto) {
        log.info(LoggerConstant.MODEL_CREATE.getMessage());
        Model model = modelDtoMapper.map(modelDto);
        model.setId(null);
        modelDao.add(model);
        log.info(LoggerConstant.MODEL_CREATE_SUCCESS.getMessage(), model.getId());
        return modelDtoMapper.map(model);
    }

    @Override
    public List<ScooterDto> findModelScooters(Long id) {
        log.info(LoggerConstant.MODEL_SCOOTERS_SEARCHED.getMessage(), id);
        modelDao.findById(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
        return modelDao.findScootersByModelId(id).stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewDto> findModelReviews(Long id) {
        modelDao.findById(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
        return modelDao.findReviewsByModelId(id).stream()
                .map(reviewDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public RateDto findCurrentModelRate(Long id) {
        modelDao.findById(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(ExceptionConstant.MODEL_NOT_EXIST.getMessage(),
                    HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
        return rateDtoMapper.map(modelDao.findCurrentRateByModelId(id));
    }
}
