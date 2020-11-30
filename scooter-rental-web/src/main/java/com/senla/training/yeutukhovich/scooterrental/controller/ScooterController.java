package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.entity.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.service.scooter.ScooterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/scooters")
public class ScooterController {

    private final ScooterService scooterService;

    @Autowired
    public ScooterController(ScooterService scooterService) {
        this.scooterService = scooterService;
    }

    @GetMapping
    public List<ScooterDto> findAll() {
        return scooterService.findAll();
    }

    @GetMapping("/{id}")
    public ScooterDto findById(@PathVariable("id") Long id) {
        return scooterService.findById(id);
    }

    @PostMapping("/{id}")
    public ScooterDto deleteById(@PathVariable("id") Long id) {
        return scooterService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ScooterDto updateById(@PathVariable("id") Long id, @RequestBody ScooterDto scooterDto) {
        return scooterService.updateById(id, scooterDto);
    }

    @PostMapping()
    public ScooterDto create(@RequestBody ScooterDto scooterDto) {
        return scooterService.create(scooterDto);
    }

    @GetMapping("/{id}/distanceTravelled")
    public Map<String, Long> findDistanceTravelledByScooterId(@PathVariable("id") Long id) {
        Map<String, Long> responseMap = new LinkedHashMap<>();
        responseMap.put("scooterId", id);
        responseMap.put("distanceTravelledMeters", Long.valueOf(scooterService.findDistanceTravelledByScooterId(id)));
        return responseMap;
    }

    @GetMapping("/{id}/rents")
    public List<RentDto> findRentsByScooterId(@PathVariable("id") Long id) {
        return scooterService.findSortedByCreationScooterRents(id);
    }

    @GetMapping("/active")
    public List<ScooterDto> findActiveRentScooters() {
        return scooterService.findActiveRentScooters();
    }
}
