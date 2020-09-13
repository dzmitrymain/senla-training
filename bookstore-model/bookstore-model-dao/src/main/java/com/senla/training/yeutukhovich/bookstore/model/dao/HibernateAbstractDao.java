package com.senla.training.yeutukhovich.bookstore.model.dao;

import com.senla.training.yeutukhovich.bookstore.model.dao.util.HibernateUtil;
import com.senla.training.yeutukhovich.bookstore.model.domain.AbstractEntity;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public abstract class HibernateAbstractDao<T extends AbstractEntity, PK extends Serializable>
        implements GenericDao<T, PK> {

    @Autowired
    protected HibernateUtil hibernateUtil;

    private Class<T> type;

    protected HibernateAbstractDao(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<T> findAll() {
        Session session = hibernateUtil.getCurrentSession();
        CriteriaQuery<T> criteriaQuery = session.getCriteriaBuilder().createQuery(type);
        Root<T> entities = criteriaQuery.from(type);
        criteriaQuery.select(entities);
        return session.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public Optional<T> findById(PK id) {
        return Optional.ofNullable(hibernateUtil.getCurrentSession().get(type, id));
    }

    @Override
    @SuppressWarnings("unchecked")
    public PK add(T entity) {
        return (PK) hibernateUtil.getCurrentSession().save(entity);
    }

    @Override
    public void update(T entity) {
        hibernateUtil.getCurrentSession().update(entity);
    }
}
