package com.senla.training.yeutukhovich.bookstore.ui.util.constant;

public enum RequestParameterConstant {

    FILE_NAME("fileName"),
    CUSTOMER_DATA("customerData"),
    REQUESTER_DATA("requesterData"),
    BOOK_ID("bookId");

    private String parameterName;

    RequestParameterConstant(String parameterName) {
        this.parameterName = parameterName;
    }

    public String getParameterName() {
        return parameterName;
    }
}
