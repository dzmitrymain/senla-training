package com.senla.training.yeutukhovich.scooterrental.service.discount;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;

import javax.validation.Valid;
import java.util.List;

public interface DiscountService {

    List<DiscountDto> findAllActiveDiscounts();

    List<DiscountDto> findAll();

    DiscountDto findById(Long id);

    DiscountDto deleteById(Long id);

    DiscountDto updateById(Long id, @Valid DiscountDto discountDto);

    DiscountDto create(@Valid DiscountDto discountDto);
}
