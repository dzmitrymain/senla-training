package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.ReviewDto;
import com.senla.training.yeutukhovich.scooterrental.service.review.ReviewService;
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
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<ReviewDto> findAll() {
        return reviewService.findAll();
    }

    @GetMapping("/{id}")
    public ReviewDto findById(@PathVariable("id") Long id) {
        return reviewService.findById(id);
    }

    @PostMapping("/{id}")
    public ReviewDto deleteById(@PathVariable("id") Long id) {
        return reviewService.deleteById(id);
    }

    @PutMapping("/{id}")
    public ReviewDto updateById(@PathVariable("id") Long id, @RequestBody ReviewDto reviewDto) {
        return reviewService.updateById(id, reviewDto);
    }

    @PostMapping()
    public ReviewDto create(@RequestBody ReviewDto reviewDto) {
        return reviewService.create(reviewDto);
    }
}
