package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum ExceptionConstant {

    ENTITY_NOT_EXIST("%s does not exist.");

    private String message;

    ExceptionConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
