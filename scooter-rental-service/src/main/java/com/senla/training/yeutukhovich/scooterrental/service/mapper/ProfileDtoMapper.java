package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;
import org.springframework.stereotype.Component;

@Component
public class ProfileDtoMapper {

    public ProfileDto map(Profile profile) {
        if (profile == null) {
            return null;
        }
        return new ProfileDto(
                profile.getId(),
                profile.getUser().getUsername(),
                profile.getUser().getRole().name(),
                profile.getUser().getEnabled(),
                profile.getUser().getCreationDate(),
                profile.getLocation().getLocationName(),
                profile.getName(),
                profile.getSurname(),
                profile.getEmail(),
                profile.getPhoneNumber());
    }
}
