package com.senla.training.yeutukhovich.bookstore.repository;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.util.Arrays;

public class EntityRepository {

    private static final double ARRAY_EXPANSION_CONSTANT = 1.5;

    private AbstractEntity[] entities;

    public EntityRepository(AbstractEntity[] entities) {
        this.entities = entities;
    }

    public AbstractEntity[] findAll() {
        AbstractEntity[] safeArray = Arrays.copyOf(entities, entities.length - calculateNullNumber(entities));
        for (int i = 0, j = 0; i < entities.length; i++) {
            if (entities[i] != null) {
                safeArray[j++] = entities[i].clone();
            }
        }
        return safeArray;
    }

    public AbstractEntity findById(int id) {
        for (AbstractEntity abstractEntity : entities) {
            if (abstractEntity != null && abstractEntity.getId() == id) {
                return abstractEntity.clone();
            }
        }
        return null;
    }

    public void update(AbstractEntity abstractEntity) {
        if (abstractEntity != null) {
            for (int i = 0; i < entities.length; i++) {
                if (entities[i] != null && entities[i].getId() == abstractEntity.getId()) {
                    entities[i] = abstractEntity.clone();
                    return;
                }
            }
        }
    }

    public void add(AbstractEntity abstractEntity) {
        if (abstractEntity != null) {
            for (int i = 0; i < entities.length; i++) {
                if (entities[i] == null) {
                    entities[i] = abstractEntity.clone();
                    return;
                }
            }
            entities = expandArray(entities);
            // стараемся избегать рекурсий, это не приветствуется в промышленном программировании
            // уж лучше дубляж кода, а в идеале - выносим общую часть и переиспользуем
            add(abstractEntity);
        }
    }

    private AbstractEntity[] expandArray(AbstractEntity[] entities) {
        return Arrays.copyOf(entities, (int) ((entities.length * ARRAY_EXPANSION_CONSTANT) + 1));
    }

    private int calculateNullNumber(AbstractEntity[] entities) {
        int nullNumber = 0;
        for (AbstractEntity abstractEntity : entities) {
            if (abstractEntity == null) {
                nullNumber++;
            }
        }
        return nullNumber;
    }
}
