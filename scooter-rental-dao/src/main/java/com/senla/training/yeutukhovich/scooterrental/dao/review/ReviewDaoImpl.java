package com.senla.training.yeutukhovich.scooterrental.dao.review;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Review;
import com.senla.training.yeutukhovich.scooterrental.domain.Review_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ReviewDaoImpl extends AbstractDao<Review, Long> implements ReviewDao {

    public ReviewDaoImpl() {
        super(Review.class);
    }

    @Override
    public Double findAverageScore() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);
        Root<Review> reviews = criteriaQuery.from(Review.class);
        CriteriaBuilder.Coalesce<Double> coalesce = criteriaBuilder.coalesce();
        coalesce.value(criteriaBuilder.avg(reviews.get(Review_.score)));
        coalesce.value(0.0);
        criteriaQuery.select(coalesce);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }
}
