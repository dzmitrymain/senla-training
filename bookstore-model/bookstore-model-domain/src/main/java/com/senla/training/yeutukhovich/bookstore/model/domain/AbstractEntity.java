package com.senla.training.yeutukhovich.bookstore.model.domain;

import java.util.Objects;

public abstract class AbstractEntity {

    public abstract Long getId();

    public abstract void setId(Long id);

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) o;

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
