package com.senla.training.yeutukhovich.scooterrental.dao.location;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Location;
import com.senla.training.yeutukhovich.scooterrental.domain.Location_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class LocationDaoImpl extends AbstractDao<Location, Long> implements LocationDao {

    public LocationDaoImpl() {
        super(Location.class);
    }

    @Override
    public List<Location> findSortedAllLocationsByName() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Location> criteriaQuery = criteriaBuilder.createQuery(Location.class);
        Root<Location> locations = criteriaQuery.from(Location.class);
        criteriaQuery.orderBy(criteriaBuilder.asc(locations.get(Location_.locationName)));
        criteriaQuery.select(locations);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
