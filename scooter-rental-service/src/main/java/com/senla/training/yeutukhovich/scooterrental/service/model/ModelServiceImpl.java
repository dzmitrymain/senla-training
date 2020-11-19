package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.dto.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.DiscountDtoMapper;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ModelServiceImpl implements ModelService {

    private static final String ENTITY_NAME = "Model";

    private final ModelDao modelDao;
    private final ModelDtoMapper modelDtoMapper;
    private final ScooterDtoMapper scooterDtoMapper;
    private final ReviewDtoMapper reviewDtoMapper;
    private final RateDtoMapper rateDtoMapper;
    private final DiscountDtoMapper discountDtoMapper;


    @Autowired
    public ModelServiceImpl(ModelDao modelDao,
                            ModelDtoMapper modelDtoMapper,
                            ScooterDtoMapper scooterDtoMapper,
                            ReviewDtoMapper reviewDtoMapper,
                            RateDtoMapper rateDtoMapper,
                            DiscountDtoMapper discountDtoMapper) {
        this.modelDao = modelDao;
        this.modelDtoMapper = modelDtoMapper;
        this.scooterDtoMapper = scooterDtoMapper;
        this.reviewDtoMapper = reviewDtoMapper;
        this.rateDtoMapper = rateDtoMapper;
        this.discountDtoMapper = discountDtoMapper;
    }

    @Override
    @Transactional
    public List<ModelDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return modelDao.findAll().stream()
                .map(modelDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ModelDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return modelDtoMapper.map(findModelById(id));
    }

    @Override
    @Transactional
    public ModelDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Model modelToDelete = findModelById(id);
        modelDao.delete(modelToDelete);
        return modelDtoMapper.map(modelToDelete);
    }

    @Override
    @Transactional
    public ModelDto updateById(Long id, ModelDto modelDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findModelById(id);
        if (!id.equals(modelDto.getId())) {
            modelDto.setId(id);
        }
        return modelDtoMapper.map(modelDao.update(modelDtoMapper.map(modelDto)));
    }

    @Override
    @Transactional
    public ModelDto create(ModelDto modelDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Model model = modelDtoMapper.map(modelDto);
        model.setId(null);
        modelDao.add(model);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), model.getId());
        return modelDtoMapper.map(model);
    }

    @Override
    @Transactional
    public List<ScooterDto> findModelScooters(Long id) {
        log.info(LoggerConstant.MODEL_SCOOTERS_SEARCHED.getMessage(), id);
        return findModelById(id).getScooters().stream()
                .map(scooterDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<ReviewDto> findModelReviews(Long id) {
        log.info(LoggerConstant.MODEL_REVIEWS.getMessage(), id);
        return findModelById(id).getReviews().stream()
                .map(reviewDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RateDto findCurrentModelRate(Long id) {
        log.info(LoggerConstant.MODEL_RATE.getMessage(), id);
        findModelById(id);
        return rateDtoMapper.map(modelDao.findCurrentRateByModelId(id));
    }

    @Override
    @Transactional
    public DiscountDto findCurrentModelDiscount(Long id) {
        log.info(LoggerConstant.MODEL_DISCOUNT.getMessage(), id);
        findModelById(id);
        return discountDtoMapper.map(modelDao.findCurrentDiscountByModelId(id));
    }

    private Model findModelById(Long modelId) {
        return modelDao.findById(modelId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
