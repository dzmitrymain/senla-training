package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum ApplicationConstant {

    RESOURCE_ROOT_FOLDER("resources"),
    CVS_FORMAT_TYPE(".cvs");

    private String constant;

    ApplicationConstant(String constant) {
        this.constant = constant;
    }

    public String getConstant() {
        return constant;
    }
}
