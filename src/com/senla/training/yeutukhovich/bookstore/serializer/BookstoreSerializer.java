package com.senla.training.yeutukhovich.bookstore.serializer;

import com.senla.training.yeutukhovich.bookstore.domain.AbstractEntity;

import java.io.*;
import java.util.List;

public class BookstoreSerializer {

    private static BookstoreSerializer instance;

    private BookstoreSerializer() {

    }

    public static BookstoreSerializer getInstance() {
        if (instance == null) {
            instance = new BookstoreSerializer();
        }
        return instance;
    }

    public <T extends AbstractEntity> void serializeBookstore(List<T> entities, String path) {

        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(path))) {
            out.writeObject(entities);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public <T extends AbstractEntity> List<T> deserializeBookstore(String path) {
        List<T> entities = null;

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(path))) {
            entities = (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return entities;
    }
}

