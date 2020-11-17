package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.ModelDto;
import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.service.model.ModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/models")
public class ModelController {

    private final ModelService modelService;

    @Autowired
    public ModelController(ModelService modelService) {
        this.modelService = modelService;
    }

    @GetMapping
    public List<ModelDto> findAll() {
        return modelService.findAll();
    }

    @GetMapping("/{id}")
    public ModelDto findById(@PathVariable("id") Long id) {
        return modelService.findById(id);
    }

    @PostMapping("/{id}")
    public ModelDto deleteById(@PathVariable("id") Long id) {
        return modelService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ModelDto updateModel(@PathVariable("id") Long id, @RequestBody ModelDto modelDto) {
        return modelService.updateModel(id, modelDto);
    }

    @PostMapping()
    public ModelDto createModel(@RequestBody ModelDto modelDto) {
        return modelService.createModel(modelDto);
    }

    @GetMapping("/{id}/scooters")
    public List<ScooterDto> findScootersByModelId(@PathVariable Long id) {
        return modelService.findModelScooters(id);
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewDto> findReviewsByModelId(@PathVariable Long id) {
        return modelService.findModelReviews(id);
    }

    @GetMapping("/{id}/rate")
    public RateDto findCurrentModelRate(@PathVariable("id") Long id){
        return modelService.findCurrentModelRate(id);
    }


}
