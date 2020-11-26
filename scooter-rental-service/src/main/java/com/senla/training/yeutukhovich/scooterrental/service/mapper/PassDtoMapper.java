package com.senla.training.yeutukhovich.scooterrental.service.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PassDtoMapper {

    private final UserDtoMapper userDtoMapper;
    private final ModelDtoMapper modelDtoMapper;

    @Autowired
    public PassDtoMapper(UserDtoMapper userDtoMapper, ModelDtoMapper modelDtoMapper) {
        this.userDtoMapper = userDtoMapper;
        this.modelDtoMapper = modelDtoMapper;
    }

    public PassDto map(Pass pass) {
        if (pass == null) {
            return null;
        }
        return new PassDto(
                pass.getId(),
                userDtoMapper.map(pass.getUser()),
                modelDtoMapper.map(pass.getModel()),
                pass.getCreationDate(),
                pass.getExpiredDate(),
                pass.getTotalMinutes(),
                pass.getRemainingMinutes(),
                pass.getPrice());
    }

    public Pass map(PassDto passDto) {
        if (passDto == null) {
            return null;
        }
        Pass pass = new Pass();
        pass.setId(passDto.getId());
        pass.setUser(userDtoMapper.map(passDto.getUserDto()));
        pass.setModel(modelDtoMapper.map(passDto.getModelDto()));
        pass.setCreationDate(passDto.getCreationDate());
        pass.setExpiredDate(passDto.getExpiredDate());
        pass.setTotalMinutes(passDto.getTotalMinutes());
        pass.setRemainingMinutes(passDto.getRemainingMinutes());
        pass.setPrice(passDto.getPrice());
        return pass;
    }
}
