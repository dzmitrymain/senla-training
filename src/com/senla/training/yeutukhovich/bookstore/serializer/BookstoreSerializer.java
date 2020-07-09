package com.senla.training.yeutukhovich.bookstore.serializer;

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
            System.err.println(e.getMessage());
        }
    }

    public ApplicationState deserializeBookstore(String path) {
        ApplicationState applicationState = null;
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(path))) {
            applicationState = (ApplicationState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return applicationState;
    }
}

