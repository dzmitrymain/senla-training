package com.senla.training.yeutukhovich.scooterrental.dao.rate;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RateDaoImpl extends AbstractDao<Rate, Long> implements RateDao {

    private static final String FIND_ALL_ACTUAL_RATES = "SELECT r1 FROM Rate r1 LEFT JOIN Rate r2 ON(r2.model=r1.model AND " +
            "r2.creationDate>r1.creationDate) WHERE r2.creationDate IS NULL";

    public RateDaoImpl() {
        super(Rate.class);
    }

    @Override
    public List<Rate> findAllActualRates() {
        return entityManager.createQuery(FIND_ALL_ACTUAL_RATES, Rate.class).getResultList();
    }
}
