package com.senla.training.yeutukhovich.scooterrental.dao.discount;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DiscountDaoImpl extends AbstractDao<Discount, Long> implements DiscountDao {

    private static final String FIND_ALL_ACTIVE_DISCOUNTS = "SELECT * FROM discounts WHERE (model_id, id) in " +
            "(SELECT model_id, MAX(id) FROM discounts WHERE end_date >= :now AND" +
            " start_date <= :now GROUP BY model_id)";

    public DiscountDaoImpl() {
        super(Discount.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Discount> findAllActiveDiscounts() {
        Query query = entityManager.createNativeQuery(FIND_ALL_ACTIVE_DISCOUNTS, Discount.class);
        query.setParameter("now", LocalDateTime.now());
        return query.getResultList();
    }
}
