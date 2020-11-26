package com.senla.training.yeutukhovich.scooterrental.service.profile;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;

import javax.validation.Valid;
import java.util.List;

public interface ProfileService {

    List<ProfileDto> findAll();

    ProfileDto findById(Long id);

    ProfileDto deleteById(Long id);

    ProfileDto updateById(Long id, @Valid ProfileDto profileDto);

    ProfileDto create(@Valid ProfileDto profileDto);
}
