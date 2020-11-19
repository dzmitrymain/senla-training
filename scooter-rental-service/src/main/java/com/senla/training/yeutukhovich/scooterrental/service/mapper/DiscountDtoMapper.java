package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.dto.DiscountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DiscountDtoMapper {

    private final ModelDtoMapper modelDtoMapper;

    @Autowired
    public DiscountDtoMapper(ModelDtoMapper modelDtoMapper) {
        this.modelDtoMapper = modelDtoMapper;
    }

    public DiscountDto map(Discount discount) {
        if (discount == null) {
            return null;
        }
        return new DiscountDto(
                discount.getId(),
                modelDtoMapper.map(discount.getModel()),
                discount.getStartDate(),
                discount.getEndDate(),
                discount.getDiscount());
    }

    public Discount map(DiscountDto discountDto) {
        if (discountDto == null) {
            return null;
        }
        Discount discount = new Discount();
        discount.setId(discountDto.getId());
        discount.setModel(modelDtoMapper.map(discountDto.getModelDto()));
        discount.setStartDate(discountDto.getStartDate());
        discount.setEndDate(discountDto.getEndDate());
        discount.setDiscount(discountDto.getDiscount());
        return discount;
    }
}
