package com.senla.training.yeutukhovich.scooterrental.dao.spot;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import org.springframework.stereotype.Repository;

@Repository
public class SpotDaoImpl extends AbstractDao<Spot, Long> implements SpotDao {

    public SpotDaoImpl() {
        super(Spot.class);
    }
}
