package com.senla.training.yeutukhovich.bookstore.model.dao.book;

import com.senla.training.yeutukhovich.bookstore.model.dao.HibernateAbstractDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book_;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order_;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.persistence.metamodel.SingularAttribute;
import java.util.Date;
import java.util.List;

@Component
public class BookDaoImpl extends HibernateAbstractDao<Book, Long> implements BookDao {

    public BookDaoImpl() {
        super(Book.class);
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
        Root<Book> books = criteriaQuery.from(Book.class);
        Join<Book, Order> orders = books.join(Book_.orders);
        criteriaQuery.select(books);
        criteriaQuery.distinct(true);
        criteriaQuery.where(cb.equal(orders.get(Order_.state), OrderState.COMPLETED.toString()),
                cb.between(orders.get(Order_.completionDate), startDate, endDate));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Book> findStaleBooksBetweenDates(Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
        Root<Book> books = criteriaQuery.from(Book.class);

        Subquery<Long> criteriaSubquery = criteriaQuery.subquery(Long.class);
        Root<Book> subqueryBooks = criteriaSubquery.from(Book.class);
        Join<Book, Order> subqueryOrders = subqueryBooks.join(Book_.orders);
        criteriaSubquery.where(cb.between(subqueryOrders.get(Order_.completionDate), startDate, endDate),
                cb.equal(subqueryOrders.get(Order_.state), OrderState.COMPLETED.toString()));
        criteriaSubquery.distinct(true);
        criteriaSubquery.select(subqueryBooks.get(Book_.id));
        criteriaQuery.where(cb.lessThanOrEqualTo(books.get(Book_.replenishmentDate), startDate),
                books.get(Book_.id).in(criteriaSubquery).not(), cb.equal(books.get(Book_.isAvailable), true));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = cb.createQuery(Book.class);
        Root<Book> books = criteriaQuery.from(Book.class);

        Subquery<Long> criteriaSubquery = criteriaQuery.subquery(Long.class);
        Root<Book> subqueryBooks = criteriaSubquery.from(Book.class);
        Join<Book, Order> subqueryOrders = subqueryBooks.join(Book_.orders);
        criteriaSubquery.where(cb.between(subqueryOrders.get(Order_.completionDate), startDate, endDate),
                cb.equal(subqueryOrders.get(Order_.state), OrderState.COMPLETED.toString()));
        criteriaSubquery.distinct(true);
        criteriaSubquery.select(subqueryBooks.get(Book_.id));

        criteriaQuery.where(books.get(Book_.id).in(criteriaSubquery).not());
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Book> findSortedAllBooksByAvailability() {
        return findAllSortedBooks(Book_.isAvailable, false);
    }

    @Override
    public List<Book> findSortedAllBooksByEditionYear() {
        return findAllSortedBooks(Book_.editionYear, true);
    }

    @Override
    public List<Book> findSortedAllBooksByPrice() {
        return findAllSortedBooks(Book_.price, true);
    }

    @Override
    public List<Book> findSortedAllBooksByReplenishmentDate() {
        return findAllSortedBooks(Book_.replenishmentDate, false);
    }

    @Override
    public List<Book> findSortedAllBooksByTitle() {
        return findAllSortedBooks(Book_.title, true);
    }

    private List<Book> findAllSortedBooks(SingularAttribute<Book, ?> singularAttribute, boolean ascOrder) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> criteriaQuery = criteriaBuilder.createQuery(Book.class);
        Root<Book> books = criteriaQuery.from(Book.class);
        criteriaQuery.select(books);
        if (ascOrder) {
            criteriaQuery.orderBy(criteriaBuilder.asc(books.get(singularAttribute)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(books.get(singularAttribute)));
        }
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
