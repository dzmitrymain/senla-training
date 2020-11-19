package com.senla.training.yeutukhovich.scooterrental.dao.model;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;

public interface ModelDao extends GenericDao<Model, Long> {

    Rate findCurrentRateByModelId(Long id);

    Discount findCurrentDiscountByModelId(Long id);
}
