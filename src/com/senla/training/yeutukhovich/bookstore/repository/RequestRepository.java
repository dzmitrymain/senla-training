package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class RequestRepository implements IRepository<Request> {

    private static IRepository<Request> instance;

    private List<Request> requests = new ArrayList<>();

    private RequestRepository() {

    }

    public static IRepository<Request> getInstance() {
        if (instance == null) {
            instance = new RequestRepository();
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
            entity.setId(IdGenerator.getInstance().getNextRequestIdNumber());
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
