package com.senla.training.yeutukhovich.scooterrental.dao.rate;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;

import java.util.List;

public interface RateDao extends GenericDao<Rate, Long> {

    List<Rate> findAllActualRates();
}
