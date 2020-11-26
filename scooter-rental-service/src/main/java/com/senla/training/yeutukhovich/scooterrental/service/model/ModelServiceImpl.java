package com.senla.training.yeutukhovich.scooterrental.service.model;

import com.senla.training.yeutukhovich.scooterrental.dao.model.ModelDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
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
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
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
    public ModelDto updateById(Long id, @Valid ModelDto modelDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findModelById(id);
        if (!id.equals(modelDto.getId())) {
            modelDto.setId(id);
        }
        return modelDtoMapper.map(modelDao.update(modelDtoMapper.map(modelDto)));
    }

    @Override
    @Transactional
    public ModelDto create(@Valid ModelDto modelDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Model model = modelDtoMapper.map(modelDto);
        model.setId(null);
        modelDao.add(model);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, model.getId());
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
        return rateDtoMapper.map(modelDao.findCurrentRateByModelId(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), "Rate"), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    @Override
    @Transactional
    public DiscountDto findCurrentModelDiscount(Long id) {
        log.info(LoggerConstant.MODEL_DISCOUNT.getMessage(), id);
        findModelById(id);
        return discountDtoMapper.map(modelDao.findCurrentDiscountByModelId(id).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), "Discount"), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        }));
    }

    @Override
    @Transactional
    public BigDecimal findCurrentModelPrice(Long id) {
        RateDto rateDto = findCurrentModelRate(id);
        final LocalDate currentDate = LocalDate.now();
        BigDecimal currentPerHourPrice;
        if (currentDate.getDayOfWeek() == DayOfWeek.SATURDAY || currentDate.getDayOfWeek() == DayOfWeek.SUNDAY) {
            currentPerHourPrice = rateDto.getWeekendPerHour();
        } else {
            currentPerHourPrice = rateDto.getPerHour();
        }
        Optional<Discount> discount;
        if ((discount = modelDao.findCurrentDiscountByModelId(id)).isPresent()) {
            BigDecimal discountCoefficient = BigDecimal.valueOf(100)
                    .subtract(discount.get().getDiscount())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);
            currentPerHourPrice = currentPerHourPrice.multiply(discountCoefficient);
        }
        return currentPerHourPrice.setScale(2,RoundingMode.HALF_EVEN);
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
