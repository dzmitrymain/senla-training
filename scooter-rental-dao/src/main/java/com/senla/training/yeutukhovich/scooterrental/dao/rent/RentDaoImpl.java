package com.senla.training.yeutukhovich.scooterrental.dao.rent;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass_;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class RentDaoImpl extends AbstractDao<Rent, Long> implements RentDao {

    public RentDaoImpl() {
        super(Rent.class);
    }

    @Override
    public List<Rent> findAllActiveRents() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> criteriaQuery = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        criteriaQuery.where(criteriaBuilder.equal(rents.get(Rent_.active), true));
        criteriaQuery.select(rents);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public BigDecimal findTotalProfit() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = criteriaBuilder.createQuery(BigDecimal.class);

        Subquery<BigDecimal> subquery = criteriaQuery.subquery(BigDecimal.class);
        Root<Pass> subqueryPasses = subquery.from(Pass.class);
        subquery.where(criteriaBuilder.notEqual(subqueryPasses.get(Pass_.price), BigDecimal.ZERO));
        subquery.select(criteriaBuilder.sum(subqueryPasses.get(Pass_.price)));

        Root<Rent> rents = criteriaQuery.from(Rent.class);
        criteriaQuery.where(criteriaBuilder.equal(rents.get(Rent_.active), false));
        Expression<BigDecimal> rentPriceSum = criteriaBuilder.sum(rents.get(Rent_.price));
        Expression<BigDecimal> rentPenaltySum = criteriaBuilder.sum(rents.get(Rent_.overtimePenalty));
        Expression<BigDecimal> rentPricePenaltySum = criteriaBuilder.sum(rentPriceSum, rentPenaltySum);
        criteriaQuery.select(criteriaBuilder.sum(rentPricePenaltySum, subquery));
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<Rent> findAllExpiredActiveRents() {
        final LocalDateTime currentDateTime = LocalDateTime.now();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> criteriaQuery = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        criteriaQuery.where(criteriaBuilder.equal(rents.get(Rent_.active), true),
                criteriaBuilder.lessThan(rents.get(Rent_.expiredDate), currentDateTime));
        criteriaQuery.select(rents);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
