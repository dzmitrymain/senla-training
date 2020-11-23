package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum LoggerConstant {

    RENT_START("Rent by User[id={}] and Scooter[id={}] is creating. Payment type: {}."),
    RENT_START_SUCCESS("Rent[id={}] by User[id={}] and Scooter[id={}] has been created."),
    RENT_END("Rent[id={}] is completing."),
    RENT_END_SUCCESS("Rent[id={}] has been completed with overtime penalty: {}."),
    RENT_PASS_CREATED("Pass[id={}] for user[id={}] on model=[id={}] with total minutes: [{}] has been created."),
    RENTS_ACTIVE("Active rents searched"),
    RENTS_TOTAL_PROFIT("Total profit searched."),
    RENTS_EXPIRED_ACTIVE("Expired active rents searched"),

    USER_PASSES("Active passes by user[id={}] searched."),
    USER_RENTS("Rents by user[id={}] searched."),
    USER_CHANGE_PASSWORD("User[id={}] is changing password."),

    DISCOUNTS_ACTIVE("Active discounts searched."),

    RATES_ACTUAL("Actual rates searched"),

    PASSES_ACTIVE("All active passes searched."),

    SCOOTER_DISTANCE("Distance travelled by scooter[id={}] searched."),
    SCOOTER_RENTS("Rents by scooter[id={}] searched."),

    MODEL_SCOOTERS_SEARCHED("Scooters by model[id={}] searched."),
    MODEL_REVIEWS("Reviews by model[id={}] searched."),
    MODEL_RATE("Current rate by model[id={}] searched."),
    MODEL_DISCOUNT("Current discount by model[id={}] searched."),

    SPOT_SCOOTERS("Scooters by spot[id={}] searched."),
    SPOT_AVAILABLE_SCOOTERS("Available scooters by spot[id={}] searched."),
    SPOT_DISTANCES("Distances from [{}, {}] to spots searched."),

    LOCATION_PROFILES("Profiles by location[id={}] searched."),
    LOCATION_SPOTS("Spots by location[id={}] searched."),

    ENTITY_CREATE("{} creating."),
    ENTITY_CREATE_SUCCESS("{}[id={}] has been created."),
    ENTITY_DELETE("{}[id={}] deleting."),
    ENTITY_UPDATE("{}[id={}] updating."),
    ENTITY_SEARCHED("{}[id={}] searched."),
    ENTITIES_SEARCHED("{}(s/es) searched.");

    private String message;

    LoggerConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
