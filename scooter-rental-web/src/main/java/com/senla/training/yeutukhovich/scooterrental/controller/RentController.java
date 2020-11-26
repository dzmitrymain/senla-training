package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.RentDto;
import com.senla.training.yeutukhovich.scooterrental.dto.CreationRentDto;
import com.senla.training.yeutukhovich.scooterrental.service.rent.RentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rents")
public class RentController {

    private final RentService rentService;

    @Autowired
    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping
    public List<RentDto> findAll() {
        return rentService.findAll();
    }

    @GetMapping("/{id}")
    public RentDto findById(@PathVariable("id") Long id) {
        return rentService.findById(id);
    }

    @PostMapping("/{id}")
    public RentDto deleteById(@PathVariable("id") Long id) {
        return rentService.deleteById(id);
    }

    @PostMapping("/{id}/end")
    public RentDto completeRent(@PathVariable("id") Long id, @RequestParam("distanceTravelled") Integer distanceTravelled) {
        return rentService.completeRent(id, distanceTravelled);
    }

    @PostMapping("/start")
    public RentDto create(@RequestBody CreationRentDto creationRentDto) {
        return rentService.create(creationRentDto);
    }

    @GetMapping("/active")
    public List<RentDto> findAllActiveRents() {
        return rentService.findAllActiveRents();
    }

    @GetMapping("/profit")
    public Map<String, BigDecimal> findTotalProfit() {
        return Collections.singletonMap("profit", rentService.findTotalProfit());
    }

    @GetMapping("/activeExpired")
    public List<RentDto> findExpiredRents() {
        return rentService.findExpiredActiveRents();
    }
}
