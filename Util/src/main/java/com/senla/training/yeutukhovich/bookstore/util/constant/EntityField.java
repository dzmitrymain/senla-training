package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum EntityField {

    BOOK_ID("book_id"),
    ORDER_ID("order_id"),
    REQUEST_ID("request_id"),
    TITLE("title"),
    IS_AVAILABLE("is_available"),
    EDITION_YEAR("edition_year"),
    REPLENISHMENT_DATE("replenishment_date"),
    PRICE("price"),
    CURRENT_PRICE("current_price"),
    CREATION_DATE("creation_date"),
    COMPLETION_DATE("completion_date"),
    CUSTOMER_DATA("customer_data"),
    STATE("state"),
    IS_ACTIVE("is_active"),
    REQUESTER_DATA("requester_data");

    private String fieldName;

    EntityField(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
