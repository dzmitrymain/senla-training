package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.PassDto;
import com.senla.training.yeutukhovich.scooterrental.service.pass.PassService;
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
@RequestMapping("passes")
public class PassController {

    private final PassService passService;

    @Autowired
    public PassController(PassService passService) {
        this.passService = passService;
    }

    @GetMapping
    public List<PassDto> findAll() {
        return passService.findAll();
    }

    @GetMapping("/{id}")
    public PassDto findById(@PathVariable("id") Long id) {
        return passService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public PassDto deleteById(@PathVariable("id") Long id) {
        return passService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public PassDto updateById(@PathVariable("id") Long id, @RequestBody PassDto passDto) {
        return passService.updateById(id, passDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public PassDto create(@RequestBody PassDto passDto) {
        return passService.create(passDto);
    }

    @GetMapping("/active")
    public List<PassDto> findAllActivePasses() {
        return passService.findAllActivePasses();
    }
}
