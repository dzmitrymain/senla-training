package com.senla.training.yeutukhovich.scooterrental.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Role;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    private final ProfileDtoMapper profileDtoMapper;

    public UserDtoMapper(ProfileDtoMapper profileDtoMapper) {
        this.profileDtoMapper = profileDtoMapper;
    }

    public UserDto map(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().name());
        userDto.setCreationDate(user.getCreationDate());
        userDto.setEnabled(user.getEnabled());
        if (user.getProfile() == null) {
            return userDto;
        }
        userDto.setProfileDto(profileDtoMapper.map(user.getProfile()));
        return userDto;
    }

    public User map(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setId(userDto.getId());
        user.setUsername(userDto.getUsername());
        user.setRole(Role.valueOf(userDto.getRole()));
        user.setCreationDate(userDto.getCreationDate());
        user.setEnabled(userDto.getEnabled());
        if (userDto.getProfileDto() == null) {
            return user;
        }
        user.setProfile(profileDtoMapper.map(userDto.getProfileDto()));
        return user;
    }
}
