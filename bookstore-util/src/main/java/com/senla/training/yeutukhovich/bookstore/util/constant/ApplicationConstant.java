package com.senla.training.yeutukhovich.bookstore.util.constant;

import java.util.Objects;

public class ApplicationConstant {

    public static final String CVS_FORMAT_TYPE = ".cvs";
    public static final String CVS_DIRECTORY_PATH = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "cvs/";
}
