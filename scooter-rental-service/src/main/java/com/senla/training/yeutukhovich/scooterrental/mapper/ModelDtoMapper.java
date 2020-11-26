package com.senla.training.yeutukhovich.scooterrental.mapper;

import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ModelDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ModelDtoMapper {

    @Autowired
    private RateDtoMapper rateDtoMapper;

    public ModelDto map(Model model) {
        if (model == null) {
            return null;
        }
        return new ModelDto(
                model.getId(),
                model.getModelName(),
                model.getRange(),
                model.getSpeed(),
                model.getPower());
    }

    public Model map(ModelDto modelDto) {
        if (modelDto == null) {
            return null;
        }
        Model model = new Model();
        model.setId(modelDto.getId());
        model.setModelName(modelDto.getModelName());
        model.setRange(modelDto.getRange());
        model.setSpeed(modelDto.getSpeed());
        model.setPower(modelDto.getPower());
        return model;
    }
}
