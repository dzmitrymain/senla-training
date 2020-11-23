package com.senla.training.yeutukhovich.scooterrental.dao;


import com.senla.training.yeutukhovich.scooterrental.domain.AbstractEntity;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends AbstractEntity, PK extends Serializable>
        implements GenericDao<T, PK> {

    @PersistenceContext
    protected EntityManager entityManager;

    private Class<T> type;

    protected AbstractDao(Class<T> type) {
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
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(T entity) {
        entityManager.remove(entity);
    }
}
