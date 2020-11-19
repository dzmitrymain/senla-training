package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.LocationDto;
import com.senla.training.yeutukhovich.scooterrental.dto.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.dto.SpotDto;
import com.senla.training.yeutukhovich.scooterrental.service.location.LocationService;
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
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping()
    public List<LocationDto> findSortedAllLocationsByName() {
        return locationService.findSortedAllLocationsByName();
    }

    @GetMapping("/{id}")
    public LocationDto findById(@PathVariable("id") Long id) {
        return locationService.findById(id);
    }

    @PostMapping("/{id}")
    public LocationDto deleteById(@PathVariable("id") Long id) {
        return locationService.deleteById(id);
    }

    @PutMapping("/{id}")
    public LocationDto updateById(@PathVariable("id") Long id, @RequestBody LocationDto locationDto) {
        return locationService.updateById(id, locationDto);
    }

    @PostMapping()
    public LocationDto create(@RequestBody LocationDto locationDto) {
        return locationService.create(locationDto);
    }

    @GetMapping("/{id}/profiles")
    public List<ProfileDto> findProfilesByLocationId(@PathVariable("id") Long id) {
        return locationService.findLocationProfiles(id);
    }

    @GetMapping("/{id}/spots")
    public List<SpotDto> findSpotsByLocationId(@PathVariable("id") Long id) {
        return locationService.findLocationSpots(id);
    }
}
