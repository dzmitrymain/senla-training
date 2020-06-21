package com.senla.training.yeutukhovich.bookstore.util.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileDataWriter {

    public static void writeData(String path, List<String> data) {

        if (path != null && data != null) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
                Iterator<String> iterator = data.iterator();
                while (iterator.hasNext()) {
                    bufferedWriter.write(iterator.next());
                    if (iterator.hasNext()) {
                        bufferedWriter.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
