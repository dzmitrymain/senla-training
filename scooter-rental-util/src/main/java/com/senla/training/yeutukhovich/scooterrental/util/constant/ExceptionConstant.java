package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum ExceptionConstant {

    MODEL_DELETE_FAIL("Model can't be deleted. Cause: '%s'"),
    MODEL_NOT_EXIST("Model does not exist.");

    private String message;

    ExceptionConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
