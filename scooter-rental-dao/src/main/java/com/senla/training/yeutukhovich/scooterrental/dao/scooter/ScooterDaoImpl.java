package com.senla.training.yeutukhovich.scooterrental.dao.scooter;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent_;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class ScooterDaoImpl extends AbstractDao<Scooter, Long> implements ScooterDao {

    public ScooterDaoImpl() {
        super(Scooter.class);
    }


    @Override
    public Long findDistanceTravelledByScooterId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        Join<Rent, Scooter> scooters = rents.join(Rent_.scooter);
        criteriaQuery.where(criteriaBuilder.equal(scooters.get(Scooter_.id), id),
                criteriaBuilder.equal(rents.get(Rent_.active), 0));
        CriteriaBuilder.Coalesce<Integer> coalesce = criteriaBuilder.coalesce();
        coalesce.value(rents.get(Rent_.distanceTravelled));
        coalesce.value(0);
        CriteriaBuilder.Coalesce<Long> sumCoalesce = criteriaBuilder.coalesce();
        sumCoalesce.value(criteriaBuilder.sumAsLong(coalesce));
        sumCoalesce.value(0L);
        criteriaQuery.select(sumCoalesce);
        return entityManager.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<Rent> findSortedByCreationScooterRents(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> criteriaQuery = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        Join<Rent, Scooter> scooters = rents.join(Rent_.scooter);
        criteriaQuery.where(criteriaBuilder.equal(scooters.get(Scooter_.id), id));
        criteriaQuery.orderBy(criteriaBuilder.desc(rents.get(Rent_.creationDate)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Scooter> findActiveRentScooters() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Scooter> criteriaQuery = criteriaBuilder.createQuery(Scooter.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        Join<Rent, Scooter> scooters = rents.join(Rent_.scooter);

        criteriaQuery.where(criteriaBuilder.equal(rents.get(Rent_.active), true));
        criteriaQuery.select(scooters).distinct(true);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
