package com.senla.training.yeutukhovich.scooterrental.dao.model;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Model;
import com.senla.training.yeutukhovich.scooterrental.domain.Model_;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate;
import com.senla.training.yeutukhovich.scooterrental.domain.Rate_;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.domain.Review_;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ModelDaoImpl extends AbstractDao<Model, Long> implements ModelDao {

    public ModelDaoImpl() {
        super(Model.class);
    }

    @Override
    public List<Scooter> findScootersByModelId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Scooter> criteriaQuery = cb.createQuery(Scooter.class);
        Root<Scooter> scooters = criteriaQuery.from(Scooter.class);
        Join<Scooter, Model> model = scooters.join(Scooter_.model);
        criteriaQuery.where(cb.equal(model.get(Model_.id), id));
        criteriaQuery.select(scooters);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Review> findReviewsByModelId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Review> criteriaQuery = cb.createQuery(Review.class);
        Root<Review> reviews = criteriaQuery.from(Review.class);
        Join<Review, Model> model = reviews.join(Review_.model);
        criteriaQuery.where(cb.equal(model.get(Model_.id), id));
        criteriaQuery.select(reviews);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Rate findCurrentRateByModelId(Long id) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rate> criteriaQuery = cb.createQuery(Rate.class);
        Root<Rate> rates = criteriaQuery.from(Rate.class);
        Join<Rate, Model> model = rates.join(Rate_.model);
        criteriaQuery.where(cb.equal(model.get(Model_.id), id));
        criteriaQuery.orderBy(cb.desc(rates.get(Rate_.CREATION_DATE)));
        criteriaQuery.select(rates);
        return entityManager.createQuery(criteriaQuery).setMaxResults(1).getSingleResult();
    }
}
