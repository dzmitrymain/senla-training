package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.RateDto;
import com.senla.training.yeutukhovich.scooterrental.service.rate.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rates")
public class RateController {

    private final RateService rateService;

    @Autowired
    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public List<RateDto> findAll() {
        return rateService.findAll();
    }

    @GetMapping("/{id}")
    public RateDto findById(@PathVariable("id") Long id) {
        return rateService.findById(id);
    }

    @PostMapping("/{id}")
    public RateDto deleteById(@PathVariable("id") Long id) {
        return rateService.deleteById(id);
    }

    @PutMapping("/{id}")
    public RateDto updateById(@PathVariable("id") Long id, @RequestBody RateDto rateDto) {
        return rateService.updateById(id, rateDto);
    }

    @PostMapping()
    public RateDto create(@RequestBody RateDto rateDto) {
        return rateService.create(rateDto);
    }

    @GetMapping("/actual")
    public List<RateDto> findAllActualRates() {
        return rateService.findAllActualRates();
    }
}
