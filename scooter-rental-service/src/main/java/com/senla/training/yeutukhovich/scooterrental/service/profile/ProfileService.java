package com.senla.training.yeutukhovich.scooterrental.service.profile;

import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;

import java.util.List;

public interface ProfileService {

    List<ProfileDto> findAll();

    ProfileDto findById(Long id);

    ProfileDto deleteById(Long id);

    ProfileDto updateById(Long id, ProfileDto profileDto);

    ProfileDto create(ProfileDto profileDto);

    ProfileDto findProfileByEmail(String email);

    ProfileDto findProfileByPhoneNumber(String phoneNumber);
}
