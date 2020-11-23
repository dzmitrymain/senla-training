package com.senla.training.yeutukhovich.scooterrental.dao.rent;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;

import java.math.BigDecimal;
import java.util.List;

public interface RentDao extends GenericDao<Rent, Long> {

    List<Rent> findAllActiveRents();

    BigDecimal findTotalProfit();

    List<Rent> findAllExpiredActiveRents();
}
