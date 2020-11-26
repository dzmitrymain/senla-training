package com.senla.training.yeutukhovich.scooterrental.service.pass;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;

import javax.validation.Valid;
import java.util.List;

public interface PassService {

    List<PassDto> findAll();

    PassDto findById(Long id);

    PassDto deleteById(Long id);

    PassDto updateById(Long id, @Valid PassDto passDto);

    PassDto create(@Valid PassDto passDto);

    List<PassDto> findAllActivePasses();
}
