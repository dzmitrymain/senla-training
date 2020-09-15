package com.senla.training.yeutukhovich.bookstore.model.dao;

import com.senla.training.yeutukhovich.bookstore.model.domain.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class HibernateAbstractDao<T extends AbstractEntity, PK extends Serializable>
        implements GenericDao<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> type;

    protected HibernateAbstractDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<T> findAll() {
        CriteriaQuery<T> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(type);
        Root<T> entities = criteriaQuery.from(type);
        criteriaQuery.select(entities);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(entityManager.find(type, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PK add(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return (PK) entity.getId();
    }

    @Override
    public void update(T entity) {
        entityManager.merge(entity);
    }
}
