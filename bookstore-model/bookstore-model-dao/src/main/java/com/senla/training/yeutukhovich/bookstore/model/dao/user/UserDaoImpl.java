package com.senla.training.yeutukhovich.bookstore.model.dao.user;

import com.senla.training.yeutukhovich.bookstore.model.dao.HibernateAbstractDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.User;
import com.senla.training.yeutukhovich.bookstore.model.domain.User_;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Optional;

@Repository
public class UserDaoImpl extends HibernateAbstractDao<User, Long> implements UserDao {

    public UserDaoImpl() {
        super(User.class);
    }

    @Override
    public Optional<User> findUserByUserName(String username) {
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
}
