package com.senla.training.yeutukhovich.scooterrental.service.discount;

import com.senla.training.yeutukhovich.scooterrental.dao.discount.DiscountDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.dto.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.exception.BusinessException;
import com.senla.training.yeutukhovich.scooterrental.service.mapper.DiscountDtoMapper;
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
public class DiscountServiceImpl implements DiscountService {

    private static final String ENTITY_NAME = "Discount";

    private final DiscountDao discountDao;
    private final DiscountDtoMapper discountDtoMapper;

    @Autowired
    public DiscountServiceImpl(DiscountDao discountDao, DiscountDtoMapper discountDtoMapper) {
        this.discountDao = discountDao;
        this.discountDtoMapper = discountDtoMapper;
    }

    @Override
    @Transactional
    public List<DiscountDto> findAllActiveDiscounts() {
        log.info(LoggerConstant.DISCOUNT_ACTIVE.getMessage());
        return discountDao.findAllActiveDiscounts().stream()
                .map(discountDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DiscountDto> findAll() {
        log.info(LoggerConstant.ENTITIES_SEARCHED.getMessage(), ENTITY_NAME);
        return discountDao.findAll().stream()
                .map(discountDtoMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DiscountDto findById(Long id) {
        log.info(LoggerConstant.ENTITY_SEARCHED.getMessage(), ENTITY_NAME, id);
        return discountDtoMapper.map(findDiscountById(id));
    }

    @Override
    @Transactional
    public DiscountDto deleteById(Long id) {
        log.info(LoggerConstant.ENTITY_DELETE.getMessage(), ENTITY_NAME, id);
        Discount discountToDelete = findDiscountById(id);
        discountDao.delete(discountToDelete);
        return discountDtoMapper.map(discountToDelete);
    }

    @Override
    @Transactional
    public DiscountDto updateById(Long id, DiscountDto discountDto) {
        log.info(LoggerConstant.ENTITY_UPDATE.getMessage(), ENTITY_NAME, id);
        findDiscountById(id);
        if (!id.equals(discountDto.getId())) {
            discountDto.setId(id);
        }
        return discountDtoMapper.map(discountDao.update(discountDtoMapper.map(discountDto)));
    }

    @Override
    @Transactional
    public DiscountDto create(DiscountDto discountDto) {
        log.info(LoggerConstant.ENTITY_CREATE.getMessage(), ENTITY_NAME);
        Discount discount = discountDtoMapper.map(discountDto);
        discount.setId(null);
        discountDao.add(discount);
        log.info(LoggerConstant.ENTITY_CREATE_SUCCESS.getMessage(), ENTITY_NAME, discount.getId());
        return discountDtoMapper.map(discount);
    }

    private Discount findDiscountById(Long discountId) {
        return discountDao.findById(discountId).orElseThrow(() -> {
            BusinessException exception = new BusinessException(
                    String.format(ExceptionConstant.ENTITY_NOT_EXIST.getMessage(), ENTITY_NAME), HttpStatus.NOT_FOUND);
            log.warn(exception.getMessage());
            return exception;
        });
    }
}
