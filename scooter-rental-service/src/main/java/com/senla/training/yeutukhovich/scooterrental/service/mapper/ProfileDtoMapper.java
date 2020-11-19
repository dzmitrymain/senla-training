package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProfileDtoMapper {

    private final LocationDtoMapper locationDtoMapper;

    @Autowired
    public ProfileDtoMapper(LocationDtoMapper locationDtoMapper) {
        this.locationDtoMapper = locationDtoMapper;
    }

    public ProfileDto map(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfileDto(
                profile.getId(),
                profile.getUser().getId(),
                locationDtoMapper.map(profile.getLocation()),
                profile.getName(),
                profile.getSurname(),
                profile.getEmail(),
                profile.getPhoneNumber());
    }
}
