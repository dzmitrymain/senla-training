package com.senla.training.yeutukhovich.scooterrental.service.discount;

import com.senla.training.yeutukhovich.scooterrental.dto.DiscountDto;

import java.util.List;

public interface DiscountService {

    List<DiscountDto> findAllActiveDiscounts();

    List<DiscountDto> findAll();

    DiscountDto findById(Long id);

    DiscountDto deleteById(Long id);

    DiscountDto updateById(Long id, DiscountDto discountDto);

    DiscountDto create(DiscountDto discountDto);
}
