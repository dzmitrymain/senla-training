package com.senla.training.yeutukhovich.bookstore.entityexchanger;

public abstract class AbstractCvsExchanger {

    private static final String directoryPath = "./resources/cvs/";
    private static final String fileFormat = ".cvs";

    protected String buildPath(String fileName) {
        return directoryPath + fileName + fileFormat;
    }
}
