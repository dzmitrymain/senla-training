package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dao.spot.SpotDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/spots")
public class SpotController {

    @Autowired
    private SpotDao spotDao;

    @GetMapping
    public List<Spot> findAll(){
        return spotDao.findAll();
    }
}
