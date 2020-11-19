package com.senla.training.yeutukhovich.scooterrental.dao.location;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Location;

import java.util.List;

public interface LocationDao extends GenericDao<Location, Long> {

    List<Location> findSortedAllLocationsByName();
}
