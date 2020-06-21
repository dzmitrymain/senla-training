package com.senla.training.yeutukhovich.bookstore.entityexchanger;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.util.List;


public interface EntityExchanger<T extends AbstractEntity> {

    void exportEntities(List<T> entities, String fileName);

    int importEntities(String fileName);
}
