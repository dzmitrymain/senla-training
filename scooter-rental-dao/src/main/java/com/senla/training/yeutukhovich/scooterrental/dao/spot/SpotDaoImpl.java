package com.senla.training.yeutukhovich.scooterrental.dao.spot;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent_;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter;
import com.senla.training.yeutukhovich.scooterrental.domain.Scooter_;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot;
import com.senla.training.yeutukhovich.scooterrental.domain.Spot_;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.List;

@Repository
public class SpotDaoImpl extends AbstractDao<Spot, Long> implements SpotDao {

    private static final String FIND_DISTANCES_FROM_POINT_TO_SPOTS = "SELECT s.id AS spotId," +
            " ROUND(ST_Distance_Sphere(s.coordinates, :coord)) AS distance FROM spots s ORDER BY distance";

    public SpotDaoImpl() {
        super(Spot.class);
    }

    @Override
    public List<Scooter> findAvailableScootersBySpotId(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Scooter> criteriaQuery = criteriaBuilder.createQuery(Scooter.class);

        Subquery<Scooter> subquery = criteriaQuery.subquery(Scooter.class);
        Root<Rent> subRents = subquery.from(Rent.class);
        Join<Rent, Scooter> subScooters = subRents.join(Rent_.scooter);
        Join<Scooter, Spot> subSpots = subScooters.join(Scooter_.spot);
        subquery.where(criteriaBuilder.equal(subRents.get(Rent_.active), 1),
                criteriaBuilder.equal(subSpots.get(Spot_.id), id));
        subquery.select(subScooters).distinct(true);

        Root<Scooter> scooters = criteriaQuery.from(Scooter.class);
        Join<Scooter, Spot> spots = scooters.join(Scooter_.spot);
        criteriaQuery.where(scooters.in(subquery).not(),
                criteriaBuilder.equal(spots.get(Spot_.id), id));
        criteriaQuery.select(scooters);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Tuple> findDistancesFromPointToSpots(Point point) {
        Query query = entityManager.createNativeQuery(FIND_DISTANCES_FROM_POINT_TO_SPOTS, Tuple.class);
        query.setParameter("coord", point);
        return query.getResultList();
    }
}
