package com.senla.training.yeutukhovich.bookstore.model.dao.order;

import com.senla.training.yeutukhovich.bookstore.model.dao.HibernateAbstractDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.util.HibernateUtil;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order;
import com.senla.training.yeutukhovich.bookstore.model.domain.Order_;
import com.senla.training.yeutukhovich.bookstore.model.domain.state.OrderState;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Repository
public class OrderDaoImpl extends HibernateAbstractDao<Order, Long> implements OrderDao {

    public OrderDaoImpl() {
        super(Order.class);
    }

    @Override
    public List<Order> findCompletedOrdersBetweenDates(Date startDate, Date endDate) {
        Session session = hibernateUtil.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = cb.createQuery(Order.class);
        Root<Order> orders = criteriaQuery.from(Order.class);
        criteriaQuery.where(cb.equal(orders.get(Order_.state), OrderState.COMPLETED.toString()),
                cb.between(orders.get(Order_.completionDate), startDate, endDate));
        criteriaQuery.select(orders);
        return session.createQuery(criteriaQuery).getResultList();
    }

    public BigDecimal calculateProfitBetweenDates(Date startDate, Date endDate) {
        Session session = hibernateUtil.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> criteriaQuery = cb.createQuery(BigDecimal.class);
        Root<Order> orders = criteriaQuery.from(Order.class);
        criteriaQuery.select(cb.sum(orders.get(Order_.currentBookPrice))).where(cb.equal(orders.get(Order_.state),
                OrderState.COMPLETED.toString()),
                cb.between(orders.get(Order_.completionDate), startDate, endDate));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public Long calculateCompletedOrdersNumberBetweenDates(Date startDate, Date endDate) {
        Session session = hibernateUtil.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = cb.createQuery(Long.class);
        Root<Order> orders = criteriaQuery.from(Order.class);
        criteriaQuery.select(cb.count(orders)).where(cb.equal(orders.get(Order_.state),
                OrderState.COMPLETED.toString()),
                cb.between(orders.get(Order_.completionDate), startDate, endDate));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<Order> findSortedAllOrdersByCompletionDate() {
        return findAllSortedOrders(Order_.completionDate, false);
    }

    @Override
    public List<Order> findSortedAllOrdersByPrice() {
        return findAllSortedOrders(Order_.currentBookPrice, true);
    }

    @Override
    public List<Order> findSortedAllOrdersByState() {
        return findAllSortedOrders(Order_.state, true);
    }

    private List<Order> findAllSortedOrders(SingularAttribute<Order, ?> singularAttribute, boolean ascOrder) {
        Session session = hibernateUtil.getCurrentSession();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        if (ascOrder) {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(singularAttribute)));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(singularAttribute)));
        }
        return session.createQuery(criteriaQuery).getResultList();
    }
}
