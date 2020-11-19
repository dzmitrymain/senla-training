package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum LoggerConstant {

    DISCOUNT_ACTIVE("Active discounts searched."),

    MODEL_SCOOTERS_SEARCHED("Scooters by model[id={}] searched."),
    MODEL_REVIEWS("Reviews by model[id={}] searched."),
    MODEL_RATE("Current rate by model[id={}] searched."),
    MODEL_DISCOUNT("Current discount by model[id={}] searched."),

    LOCATION_PROFILES("Profiles by location[id={}] searched."),
    LOCATION_SPOTS("Spots by location[id={}] searched."),

    ENTITY_CREATE("{} creating."),
    ENTITY_CREATE_SUCCESS("{}[id={}] has been created."),
    ENTITY_DELETE("{}[id={}] deleting."),
    ENTITY_UPDATE("{}[id={}] updating."),
    ENTITY_SEARCHED("{}[id={}] searched"),
    ENTITIES_SEARCHED("{}(s/es) searched.");

    private String message;

    LoggerConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
