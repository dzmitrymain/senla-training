package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum Fields {

    BOOK_ID("book_id"),
    ORDER_ID("order_id"),
    REQUEST_ID("request_id"),
    TITLE("title"),
    IS_AVAILABLE("is_available"),
    EDITION_DATE("edition_date"),
    REPLENISHMENT_DATE("replenishment_date"),
    PRICE("price"),
    CURRENT_PRICE("current_price"),
    CREATION_DATE("creation_date"),
    COMPLETION_DATE("completion_date"),
    CUSTOMER_DATA("customer_data"),
    STATE_TYPE("state_type"),
    IS_ACTIVE("is_active"),
    REQUESTER_DATA("requester_data");

    private String fieldName;

    Fields(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
