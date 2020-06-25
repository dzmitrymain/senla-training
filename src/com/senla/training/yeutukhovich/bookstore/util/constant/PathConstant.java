package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum PathConstant {

    DIRECTORY_PATH("./resources/cvs/"),
    FORMAT_TYPE(".cvs");

    private String pathConstant;

    PathConstant(String pathConstant) {
        this.pathConstant = pathConstant;
    }

    public String getPathConstant() {
        return pathConstant;
    }
}
