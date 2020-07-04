package com.senla.training.yeutukhovich.bookstore.util.writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class FileDataWriter {

    public static int writeData(String path, List<String> data) {

        int wroteLines = 0;

        if (path != null && data != null) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path))) {
                Iterator<String> iterator = data.iterator();
                while (iterator.hasNext()) {
                    bufferedWriter.write(iterator.next());
                    wroteLines++;
                    if (iterator.hasNext()) {
                        bufferedWriter.newLine();
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return wroteLines;
    }
}
