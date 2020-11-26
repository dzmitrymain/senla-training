package com.senla.training.yeutukhovich.scooterrental.dao.model;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;

import java.util.Optional;

public interface ModelDao extends GenericDao<Model, Long> {

    Optional<Rate> findCurrentRateByModelId(Long id);

    Optional<Discount> findCurrentDiscountByModelId(Long id);
}
