package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;

import java.util.List;
import java.util.ListIterator;

public class EntityRepository<T extends AbstractEntity> {

    private static EntityRepository<Book> bookRepositoryInstance;
    private static EntityRepository<Order> orderRepositoryInstance;
    private static EntityRepository<Request> requestRepositoryInstance;

    private List<T> entities;

    public static EntityRepository<Book> getBookRepositoryInstance() {
        if (bookRepositoryInstance == null) {
            bookRepositoryInstance = new EntityRepository<>(EntityInitializer.getBooks());
        }
        return bookRepositoryInstance;
    }

    public static EntityRepository<Order> getOrderRepositoryInstance() {
        if (orderRepositoryInstance == null) {
            orderRepositoryInstance = new EntityRepository<>(EntityInitializer.getOrders());
        }
        return orderRepositoryInstance;
    }

    public static EntityRepository<Request> getRequestRepositoryInstance() {
        if (requestRepositoryInstance == null) {
            requestRepositoryInstance = new EntityRepository<>(EntityInitializer.getRequests());
        }
        return requestRepositoryInstance;
    }

    private EntityRepository(List<T> entities) {
        this.entities = entities;
    }

    public List<T> findAll() {
        return List.copyOf(entities);
    }

    public T findById(Long id) {
        if (id != null) {
            for (T abstractEntity : entities) {
                if (abstractEntity.getId().equals(id)) {
                    return abstractEntity;
                }
            }
        }
        return null;
    }

    public void update(T abstractEntity) {
        if (abstractEntity != null) {
            ListIterator<T> listIterator = entities.listIterator();
            while (listIterator.hasNext()) {
                AbstractEntity repositoryEntity = listIterator.next();
                if (repositoryEntity.getId().equals(abstractEntity.getId())) {
                    listIterator.set(abstractEntity);
                }
            }
        }
    }

    public void add(T abstractEntity) {
        if (abstractEntity != null) {
            entities.add(abstractEntity);
        }
    }
}
