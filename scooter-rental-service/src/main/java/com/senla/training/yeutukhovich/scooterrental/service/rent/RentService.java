package com.senla.training.yeutukhovich.scooterrental.service.rent;

import com.senla.training.yeutukhovich.scooterrental.dto.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.StartRentDto;

import java.math.BigDecimal;
import java.util.List;

public interface RentService {

    List<RentDto> findAll();

    RentDto findById(Long id);

    RentDto deleteById(Long id);

    RentDto completeRent(Long id, Integer distanceTravelled);

    RentDto create(StartRentDto startRentDto);

    List<RentDto> findAllActiveRents();

    BigDecimal findTotalProfit();

    List<RentDto> findExpiredActiveRents();
}
