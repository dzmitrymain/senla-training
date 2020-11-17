package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum LoggerConstant {

    MODEL_SCOOTERS_SEARCHED("Scooters by model[id={}] searched."),
    MODEL_CREATE("Model creating"),
    MODEL_CREATE_SUCCESS("Model[id={}] has been created."),
    MODEL_DELETE("Model[id={}] deleting."),
    MODEL_UPDATE("Model[id={}] updating."),
    MODEL_SEARCHED("Model[id={}] searched"),
    MODELS_SEARCHED("Models searched.");

    private String message;

    LoggerConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
