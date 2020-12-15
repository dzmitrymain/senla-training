package com.senla.training.yeutukhovich.scooterrental.dao.rate;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RateDaoImpl extends AbstractDao<Rate, Long> implements RateDao {

    private static final String FIND_ALL_ACTUAL_RATES = "SELECT * FROM rates WHERE (model_id, creation_date) in " +
            "(SELECT model_id, MAX(creation_date) FROM rates WHERE creation_date <= :now GROUP BY model_id)";

    public RateDaoImpl() {
        super(Rate.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Rate> findAllActualRates() {
        Query query = entityManager.createNativeQuery(FIND_ALL_ACTUAL_RATES, Rate.class);
        query.setParameter("now", LocalDateTime.now());
        return query.getResultList();
    }
}
