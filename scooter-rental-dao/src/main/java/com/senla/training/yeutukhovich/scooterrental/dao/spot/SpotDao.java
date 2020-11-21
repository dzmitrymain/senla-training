package com.senla.training.yeutukhovich.scooterrental.dao.spot;

import com.senla.training.yeutukhovich.scooterrental.dao.GenericDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import org.locationtech.jts.geom.Point;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;

public interface SpotDao extends GenericDao<Spot, Long> {

    List<Scooter> findAvailableScootersBySpotId(Long id);

    List<Tuple> findDistancesFromPointToSpots(Point point);
}
