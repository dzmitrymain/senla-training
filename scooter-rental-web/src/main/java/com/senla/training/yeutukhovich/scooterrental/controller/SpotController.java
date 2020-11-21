package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.ScooterDto;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.service.spot.SpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/spots")
public class SpotController {

    private final SpotService spotService;

    @Autowired
    public SpotController(SpotService spotService) {
        this.spotService = spotService;
    }

    @GetMapping
    public List<SpotDto> findAll() {
        return spotService.findAll();
    }

    @GetMapping("/{id}")
    public SpotDto findById(@PathVariable("id") Long id) {
        return spotService.findById(id);
    }

    @PostMapping("/{id}")
    public SpotDto deleteById(@PathVariable("id") Long id) {
        return spotService.deleteById(id);
    }

    @PutMapping("/{id}")
    public SpotDto updateById(@PathVariable("id") Long id, @RequestBody SpotDto spotDto) {
        return spotService.updateById(id, spotDto);
    }

    @PostMapping()
    public SpotDto create(@RequestBody SpotDto spotDto) {
        return spotService.create(spotDto);
    }

    @GetMapping("/coordinateDistances")
    public List<Map<String, Long>> findDistancesToSpots(@RequestParam("lat") Double latitude,
                                                        @RequestParam("long") Double longitude) {
        return spotService.findDistancesToSpots(latitude, longitude);
    }

    @GetMapping("/{id}/availableScooters")
    public List<ScooterDto> findAvailableScootersBySpotId(@PathVariable("id") Long id) {
        return spotService.findAvailableScootersBySpotId(id);
    }

    @GetMapping("/{id}/scooters")
    public List<ScooterDto> findScootersBySpotId(@PathVariable("id") Long id) {
        return spotService.findSpotScooters(id);
    }
}
