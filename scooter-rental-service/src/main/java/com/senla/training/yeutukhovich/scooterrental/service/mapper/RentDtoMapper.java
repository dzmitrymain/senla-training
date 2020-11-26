package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import org.springframework.stereotype.Component;

@Component
public class RentDtoMapper {

    private final UserDtoMapper userDtoMapper;
    private final ScooterDtoMapper scooterDtoMapper;

    public RentDtoMapper(UserDtoMapper userDtoMapper, ScooterDtoMapper scooterDtoMapper) {
        this.userDtoMapper = userDtoMapper;
        this.scooterDtoMapper = scooterDtoMapper;
    }

    public RentDto map(Rent rent) {
        if (rent == null) {
            return null;
        }
        return new RentDto(
                rent.getId(),
                userDtoMapper.map(rent.getUser()),
                scooterDtoMapper.map(rent.getScooter()),
                rent.getActive(),
                rent.getCreationDate(),
                rent.getExpiredDate(),
                rent.getReturnDate(),
                rent.getPaymentType().name(),
                rent.getDistanceTravelled(),
                rent.getPrice(),
                rent.getOvertimePenalty());
    }
}
