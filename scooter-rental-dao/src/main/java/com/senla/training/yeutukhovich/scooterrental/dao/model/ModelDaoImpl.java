package com.senla.training.yeutukhovich.scooterrental.dao.model;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount;
import com.senla.training.yeutukhovich.scooterrental.domain.Discount_;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Model_;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class ModelDaoImpl extends AbstractDao<Model, Long> implements ModelDao {

    public ModelDaoImpl() {
        super(Model.class);
    }

    @Override
    public Optional<Rate> findCurrentRateByModelId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rate> criteriaQuery = cb.createQuery(Rate.class);
        Root<Rate> rates = criteriaQuery.from(Rate.class);
        Join<Rate, Model> model = rates.join(Rate_.model);
        criteriaQuery.where(cb.equal(model.get(Model_.id), id));
        criteriaQuery.orderBy(cb.desc(rates.get(Rate_.CREATION_DATE)), cb.desc(rates.get(Rate_.id)));
        criteriaQuery.select(rates);
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Discount> findCurrentDiscountByModelId(Long id) {
        final LocalDateTime currentLocalDateTime = LocalDateTime.now();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Discount> criteriaQuery = cb.createQuery(Discount.class);
        Root<Discount> discounts = criteriaQuery.from(Discount.class);
        Join<Discount, Model> model = discounts.join(Discount_.model);
        criteriaQuery.where(cb.equal(model.get(Model_.id), id),
                cb.greaterThanOrEqualTo(discounts.get(Discount_.endDate), currentLocalDateTime),
                cb.lessThanOrEqualTo(discounts.get(Discount_.startDate), currentLocalDateTime));
        criteriaQuery.orderBy(cb.desc(discounts.get(Discount_.startDate)), cb.desc(discounts.get(Discount_.id)));
        criteriaQuery.select(discounts);
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
