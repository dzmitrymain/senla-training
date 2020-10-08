package com.senla.training.yeutukhovich.bookstore.util.constant;

import java.util.Objects;

public class ApplicationConstant {

    public static final String CSV_FORMAT_TYPE = ".csv";
    public static final String CSV_DIRECTORY_PATH = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "csv/";
}
