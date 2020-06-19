package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;

import java.util.List;

public class RequestRepository implements IRepository<Request> {

    private static RequestRepository instance;

    private List<Request> requests;

    private RequestRepository(List<Request> requests) {
        this.requests = requests;
    }

    public static RequestRepository getInstance() {
        if (instance == null) {
            instance = new RequestRepository(EntityInitializer.getRequests());
        }
        return instance;
    }


    @Override
    public List<Request> findAll() {
        return List.copyOf(requests);
    }

    @Override
    public Request findById(Long id) {
        if (id != null) {
            for (Request request : requests) {
                if (request.getId().equals(id)) {
                    return request;
                }
            }
        }
        return null;
    }

    @Override
    public void add(Request entity) {
        if (entity != null && !requests.contains(entity)) {
            requests.add(entity);
        }
    }

    @Override
    public void update(Request entity) {
        if (entity != null) {
            int index = requests.indexOf(entity);
            if (index != -1) {
                requests.set(index, entity);
            }
        }
    }
}
