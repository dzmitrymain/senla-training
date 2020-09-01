package com.senla.training.yeutukhovich.bookstore.util.reader;

import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class FileDataReader {

    private FileDataReader() {

    }

    public static List<String> readData(String path) {
        List<String> data = new ArrayList<>();
        if (path != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
                data = bufferedReader.lines().collect(Collectors.toList());
            } catch (FileNotFoundException e) {
                throw new BusinessException(e.getMessage());
            } catch (IOException e) {
                throw new InternalException(e.getMessage());
            }
        }
        return data;
    }
}
