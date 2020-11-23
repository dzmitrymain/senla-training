package com.senla.training.yeutukhovich.scooterrental.dao.pass;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PassDaoImpl extends AbstractDao<Pass, Long> implements PassDao {

    public PassDaoImpl() {
        super(Pass.class);
    }

    @Override
    public List<Pass> findAllActivePasses() {
        final LocalDateTime currentDateTime = LocalDateTime.now();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pass> criteriaQuery = criteriaBuilder.createQuery(Pass.class);
        Root<Pass> passes = criteriaQuery.from(Pass.class);
        criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(passes.get(Pass_.creationDate), currentDateTime),
                criteriaBuilder.greaterThanOrEqualTo(passes.get(Pass_.expiredDate), currentDateTime),
                criteriaBuilder.greaterThan(passes.get(Pass_.remainingMinutes), 0));
        criteriaQuery.select(passes);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
