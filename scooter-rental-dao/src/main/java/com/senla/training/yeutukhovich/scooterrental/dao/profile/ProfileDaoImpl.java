package com.senla.training.yeutukhovich.scooterrental.dao.profile;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile;
import com.senla.training.yeutukhovich.scooterrental.domain.Profile_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class ProfileDaoImpl extends AbstractDao<Profile, Long> implements ProfileDao {

    public ProfileDaoImpl() {
        super(Profile.class);
    }

    @Override
    public Optional<Profile> findProfileByEmail(String email) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profile> criteriaQuery = cb.createQuery(Profile.class);
        Root<Profile> profiles = criteriaQuery.from(Profile.class);
        criteriaQuery.where(cb.equal(profiles.get(Profile_.email), email));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Profile> findProfileByPhoneNumber(String phoneNumber) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Profile> criteriaQuery = cb.createQuery(Profile.class);
        Root<Profile> profiles = criteriaQuery.from(Profile.class);
        criteriaQuery.where(cb.equal(profiles.get(Profile_.phoneNumber), phoneNumber));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
