package com.senla.training.yeutukhovich.bookstore.model.dao.request;

import com.senla.training.yeutukhovich.bookstore.model.dao.HibernateAbstractDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book_;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request_;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.util.List;

@Repository
public class RequestDaoImpl extends HibernateAbstractDao<Request, Long> implements RequestDao {

    public RequestDaoImpl() {
        super(Request.class);
    }

    @Override
    public Long closeRequestsByBookId(Long bookId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Request> update = cb.createCriteriaUpdate(Request.class);
        Root<Request> requests = update.from(Request.class);
        update.set(Request_.active, false);
        update.where(cb.equal(requests.get(Request_.book).get(Book_.id), bookId),
                cb.equal(requests.get(Request_.active), true));
        return (long) entityManager.createQuery(update).executeUpdate();
    }

    @Override
    public List<Request> findSortedAllRequestsByBookTitle() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> requests = criteriaQuery.from(Request.class);
        Join<Request, Book> books = requests.join(Request_.book);
        criteriaQuery.select(requests);
        criteriaQuery.orderBy(criteriaBuilder.asc(books.get(Book_.title)));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Request> findSortedAllRequestsByIsActive() {
        return findAllSortedRequests(Request_.active, false);
    }

    @Override
    public List<Request> findSortedAllRequestsByRequesterData() {
        return findAllSortedRequests(Request_.requesterData, true);
    }

    private List<Request> findAllSortedRequests(SingularAttribute<Request, ?> singularAttribute, boolean ascOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Request> criteriaQuery = criteriaBuilder.createQuery(Request.class);
        Root<Request> requests = criteriaQuery.from(Request.class);
        criteriaQuery.select(requests);
        if (ascOrder) {
            criteriaQuery.orderBy(criteriaBuilder.asc(requests.get(singularAttribute)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(requests.get(singularAttribute)));
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
