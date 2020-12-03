package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.ProfileDto;
import com.senla.training.yeutukhovich.scooterrental.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public List<ProfileDto> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/{id}")
    public ProfileDto findById(@PathVariable("id") Long id) {
        return profileService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public ProfileDto deleteById(@PathVariable("id") Long id) {
        return profileService.deleteById(id);
    }


    @PutMapping("/{id}")
    public ProfileDto updateById(@PathVariable("id") Long id, @RequestBody ProfileDto profileDto) {
        return profileService.updateById(id, profileDto);
    }

    @PostMapping()
    public ProfileDto create(@RequestBody ProfileDto profileDto) {
        return profileService.create(profileDto);
    }
}
