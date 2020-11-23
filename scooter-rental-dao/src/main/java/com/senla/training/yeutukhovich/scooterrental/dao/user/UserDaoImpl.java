package com.senla.training.yeutukhovich.scooterrental.dao.user;

import com.senla.training.yeutukhovich.scooterrental.dao.AbstractDao;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass;
import com.senla.training.yeutukhovich.scooterrental.domain.Pass_;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent;
import com.senla.training.yeutukhovich.scooterrental.domain.Rent_;
import com.senla.training.yeutukhovich.scooterrental.domain.User;
import com.senla.training.yeutukhovich.scooterrental.domain.User_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl extends AbstractDao<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);
        Root<User> users = criteriaQuery.from(User.class);
        criteriaQuery.where(cb.equal(users.get(User_.username), username));
        try {
            return Optional.of(entityManager.createQuery(criteriaQuery).getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Pass> findAllActiveUserPasses(Long id) {
        final LocalDateTime currentDateTime = LocalDateTime.now();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pass> criteriaQuery = criteriaBuilder.createQuery(Pass.class);
        Root<Pass> passes = criteriaQuery.from(Pass.class);
        Join<Pass, User> users = passes.join(Pass_.user);
        criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(passes.get(Pass_.creationDate), currentDateTime),
                criteriaBuilder.greaterThanOrEqualTo(passes.get(Pass_.expiredDate), currentDateTime),
                criteriaBuilder.greaterThan(passes.get(Pass_.remainingMinutes), 0),
                criteriaBuilder.equal(users.get(User_.id), id));
        criteriaQuery.select(passes);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Rent> findAllSortedByCreationUserRents(Long id) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Rent> criteriaQuery = criteriaBuilder.createQuery(Rent.class);
        Root<Rent> rents = criteriaQuery.from(Rent.class);
        Join<Rent, User> users = rents.join(Rent_.user);
        criteriaQuery.where(criteriaBuilder.equal(users.get(User_.id), id));
        criteriaQuery.select(rents);
        criteriaQuery.orderBy(criteriaBuilder.desc(rents.get(Rent_.creationDate)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
