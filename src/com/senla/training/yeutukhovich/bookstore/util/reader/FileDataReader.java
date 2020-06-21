package com.senla.training.yeutukhovich.bookstore.util.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileDataReader {

    public static List<String> readData(String path) {
        List<String> data = new ArrayList<>();
        if (path != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
                data = bufferedReader.lines().collect(Collectors.toList());
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return data;
    }
}
