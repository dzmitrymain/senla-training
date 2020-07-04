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
            // никакой печати на беке, все что не должен видеть пользователь, скоро
            // уйдет в логирование
            // все что должен видеть пользователь - передаем в кастомных экзепшнах, смотри
            // пример в моих видео
            System.err.println(e.getMessage());
        }
    }

    // отличное использование дженериков, все ок, но!
    // есть вариант проще и без анчекед каста - написать класс сущности, которая тоже Serializable
    // и которая содержит в себе все три коллекции для сериализации
    // все кладется в один файл, одним действием, никаких анчекед кастов не нужно
    public <T extends AbstractEntity> List<T> deserializeBookstore(String path) {
        List<T> entities = null;

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(path))) {
            // анчекед каст, про него раскажу на созвоне (или избегать, или глушить аннотацией)
            entities = (List<T>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return entities;
    }
}

