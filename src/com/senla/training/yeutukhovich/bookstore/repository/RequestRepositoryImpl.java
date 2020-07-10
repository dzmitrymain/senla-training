package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.generator.IdGenerator;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class RequestRepositoryImpl implements RequestRepository {

    @Autowired
    private IdGenerator idGenerator;
    private List<Request> requests = new ArrayList<>();

    private RequestRepositoryImpl() {

    }

    @Override
    public List<Request> findAll() {
        return List.copyOf(requests);
    }

    @Override
    public Optional<Request> findById(Long id) {
        return requests.stream()
                .filter(request -> request.getId().equals(id))
                .findFirst();
    }

    @Override
    public void add(Request entity) {
        if (entity != null && !requests.contains(entity)) {
            entity.setId(idGenerator.getNextRequestIdNumber());
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
