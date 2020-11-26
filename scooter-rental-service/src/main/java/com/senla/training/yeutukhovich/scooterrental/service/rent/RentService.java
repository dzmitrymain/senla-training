package com.senla.training.yeutukhovich.scooterrental.service.rent;

import com.senla.training.yeutukhovich.scooterrental.dto.CreationRentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.List;

public interface RentService {

    List<RentDto> findAll();

    RentDto findById(Long id);

    RentDto deleteById(Long id);

    RentDto completeRent(Long id, @PositiveOrZero Integer distanceTravelled);

    RentDto create(@Valid CreationRentDto creationRentDto);

    List<RentDto> findAllActiveRents();

    BigDecimal findTotalProfit();

    List<RentDto> findExpiredActiveRents();
}
