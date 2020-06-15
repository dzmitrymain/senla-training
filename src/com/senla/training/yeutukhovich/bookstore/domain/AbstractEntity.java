package com.senla.training.yeutukhovich.bookstore.domain;

public abstract class AbstractEntity {

    protected final Long id;

    protected AbstractEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
