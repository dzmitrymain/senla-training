package com.senla.training.yeutukhovich.bookstore.serializer;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;

import java.io.*;

@Singleton
public class BookstoreSerializer {

    private BookstoreSerializer() {

    }

    public void serializeBookstore(ApplicationState applicationState, String path) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(path))) {
            out.writeObject(applicationState);
        } catch (IOException e) {
            throw new InternalException(e.getMessage());
        }
    }

    public ApplicationState deserializeBookstore(String path) {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(path))) {
            return (ApplicationState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new InternalException(e.getMessage());
        }
    }
}

