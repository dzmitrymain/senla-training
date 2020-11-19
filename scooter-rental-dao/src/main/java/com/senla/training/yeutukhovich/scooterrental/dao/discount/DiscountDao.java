package com.senla.training.yeutukhovich.scooterrental.dao.discount;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;

import java.util.List;

public interface DiscountDao extends GenericDao<Discount, Long> {

    List<Discount> findAllActiveDiscounts();
}
