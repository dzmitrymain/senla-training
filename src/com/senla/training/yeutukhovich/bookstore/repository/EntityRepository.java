package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;

import java.util.Arrays;
import java.util.List;

public class EntityRepository<T extends AbstractEntity> {

    //TODO maybe Enum singleton??
    private static EntityRepository<Book> bookRepositoryInstance;
    private static EntityRepository<Order> orderRepositoryInstance;
    private static EntityRepository<Request> requestRepositoryInstance;

    private static final double ARRAY_EXPANSION_CONSTANT = 1.5;

    private T[] entities;

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

    private EntityRepository(T[] entities) {
        this.entities = entities;
    }

    public T[] findAll() {
        T[] safeArray = Arrays.copyOf(entities, entities.length - calculateNullNumber(entities));
        for (int i = 0, j = 0; i < entities.length; i++) {
            if (entities[i] != null) {
                safeArray[j++] = (T) entities[i].clone();
            }
        }
        return safeArray;
    }

    public T findById(Long id) {
        if (id != null) {
            for (T abstractEntity : entities) {
                if (abstractEntity != null && abstractEntity.getId().equals(id)) {
                    return (T) abstractEntity.clone();
                }
            }
        }
        return null;
    }

    public void update(T abstractEntity) {
        if (abstractEntity != null) {
            for (int i = 0; i < entities.length; i++) {
                if (entities[i] != null && entities[i].getId().equals(abstractEntity.getId())) {
                    entities[i] = (T) abstractEntity.clone();
                    return;
                }
            }
        }
    }

    public void add(T abstractEntity) {
        if (abstractEntity != null) {
            for (int i = 0; i < entities.length; i++) {
                if (entities[i] == null) {
                    entities[i] = (T) abstractEntity.clone();
                    return;
                }
            }
            entities = expandArray(entities);
            add(abstractEntity);
        }
    }

    private T[] expandArray(T[] entities) {
        return Arrays.copyOf(entities, (int) ((entities.length * ARRAY_EXPANSION_CONSTANT) + 1));
    }

    private int calculateNullNumber(T[] entities) {
        int nullNumber = 0;
        for (T abstractEntity : entities) {
            if (abstractEntity == null) {
                nullNumber++;
            }
        }
        return nullNumber;
    }
}
