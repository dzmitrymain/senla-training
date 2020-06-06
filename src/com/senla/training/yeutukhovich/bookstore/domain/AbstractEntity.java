package com.senla.training.yeutukhovich.bookstore.domain;

public abstract class AbstractEntity implements Cloneable {

    protected final int id;

    protected AbstractEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public AbstractEntity clone() {
        try {
            return (AbstractEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.getMessage());
        }
    }
}
