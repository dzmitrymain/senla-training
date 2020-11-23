package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum ExceptionConstant {

    USER_CHANGE_PASSWORD_WRONG("User[id=%d] try to change password with incorrect old password."),
    USER_CREATE_ADMIN_FAIL("Only admin can create user with role: ADMIN"),
    USER_ALREADY_EXIST("User with username: [%s] already exists."),

    PROFILE_EMAIL_ALREADY_EXISTS("Email [%s] already exists."),
    PROFILE_PHONE_NUMBER_ALREADY_EXISTS("Phone number [%s] already exists."),

    RENT_ALREADY_ENDS("Rent[id=%d] already ended."),
    SCOOTER_NOT_AVAILABLE("Scooter[id=%d] not available."),
    ENTITY_NOT_EXIST("%s does not exist.");

    private String message;

    ExceptionConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
