package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
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

    public Profile map(ProfileDto profileDto) {
        if (profileDto == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(profileDto.getId());
        User user = new User();
        user.setId(profileDto.getUserId());
        profile.setUser(user);
        profile.setLocation(locationDtoMapper.map(profileDto.getLocationDto()));
        profile.setName(profileDto.getName());
        profile.setSurname(profileDto.getSurname());
        profile.setEmail(profileDto.getEmail());
        profile.setPhoneNumber(profileDto.getPhoneNumber());
        return profile;
    }
}
