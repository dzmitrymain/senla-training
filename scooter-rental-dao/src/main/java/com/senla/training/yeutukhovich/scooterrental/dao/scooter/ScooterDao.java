package com.senla.training.yeutukhovich.scooterrental.dao.scooter;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;

import java.util.List;

public interface ScooterDao extends GenericDao<Scooter, Long> {

    Integer findDistanceTravelledByScooterId(Long id);

    List<Rent> findSortedByCreationScooterRents(Long id);
}
