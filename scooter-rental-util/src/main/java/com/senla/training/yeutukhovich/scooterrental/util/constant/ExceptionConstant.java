package com.senla.training.yeutukhovich.scooterrental.util.constant;

public enum ExceptionConstant {

    SOMETHING_WENT_WRONG("Something went wrong."),
    VALIDATION_FAILURE("Data validation failure."),
    SQL_EXECUTION_FAILURE("Sql statement execution failure. Check the constraints."),

    USER_CHANGE_PASSWORD_WRONG("User[id=%d] try to change password with incorrect old password."),
    USER_CREATE_ADMIN_FAIL("Only admin can create user with role: ADMIN"),
    USER_ALREADY_EXIST("User with username: [%s] already exists."),
    USER_NOT_FOUND("User with username: [%s] not found."),
    USER_AUTHORIZATION_FAIL("Invalid username or password"),
    USER_AUTHENTICATION_REQUIRED("Authentication required. Check your authorization token."),
    USER_ACCESS_DENIED("Access denied."),

    PROFILE_ALREADY_EXIST("User[id=%d] already has profile."),
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
