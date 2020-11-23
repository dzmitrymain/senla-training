package com.senla.training.yeutukhovich.scooterrental.service.pass;

import com.senla.training.yeutukhovich.scooterrental.dto.PassDto;

import java.util.List;

public interface PassService {

    List<PassDto> findAll();

    PassDto findById(Long id);

    PassDto deleteById(Long id);

    PassDto updateById(Long id, PassDto passDto);

    PassDto create(PassDto passDto);

    List<PassDto> findAllActivePasses();
}
