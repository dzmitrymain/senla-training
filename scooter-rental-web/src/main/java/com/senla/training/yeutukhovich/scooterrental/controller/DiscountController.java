package com.senla.training.yeutukhovich.scooterrental.controller;

import com.senla.training.yeutukhovich.scooterrental.dto.entity.DiscountDto;
import com.senla.training.yeutukhovich.scooterrental.service.discount.DiscountService;
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
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping
    public List<DiscountDto> findAll() {
        return discountService.findAll();
    }

    @GetMapping("/{id}")
    public DiscountDto findById(@PathVariable("id") Long id) {
        return discountService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}")
    public DiscountDto deleteById(@PathVariable("id") Long id) {
        return discountService.deleteById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public DiscountDto updateById(@PathVariable("id") Long id, @RequestBody DiscountDto discountDto) {
        return discountService.updateById(id, discountDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public DiscountDto create(@RequestBody DiscountDto discountDto) {
        return discountService.create(discountDto);
    }

    @GetMapping("/active")
    public List<DiscountDto> findAllActiveDiscounts() {
        return discountService.findAllActiveDiscounts();
    }
}
