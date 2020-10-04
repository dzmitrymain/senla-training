package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum MessageConstant {

    MENU_BORDER("========================================"),
    MENU_ITEM_BORDER("----------------------------------------"),
    MENU_NAME_INDENT("        "),
    MENU_ITEM_INDEX_DELIMITER(". "),

    SOMETHING_WENT_WRONG("Something went wrong."),
    NO_DATA_WAS_FOUND("No data was found."),
    STRING_FORMAT_UNPARSEABLE_BOOLEAN("Unparseable boolean: '%s'"),

    BOOK_NOT_EXIST("Book not exist."),
    BOOK_CANT_NULL("Book can't be null."),
    BOOK_ALREADY_REPLENISHED("Book already replenished."),
    BOOK_ALREADY_WRITTEN_OFF("Book already written off."),
    CREATION_NOT_NULL("Creation date can't be null."),

    ENTER_BOOK_ID("Please, enter book id: "),
    BOOK_NOT_AVAILABLE("Book not available."),
    BOOK_HAS_BEEN_REPLENISHED("Book has been replenished."),
    BOOK_HAS_BEEN_WRITTEN_OFF("Book has been written off."),

    ENTER_ORDER_ID("Please, enter order id: "),
    ORDER_NOT_EXIST("Order not exist."),
    WRONG_ORDER_STATE("Wrong current order state."),
    ORDER_HAS_BEEN_CREATED("Order has been created."),
    ORDER_HAS_BEEN_CANCELED("Order has been canceled."),
    ORDER_HAS_BEEN_COMPLETED("Order has been completed."),
    COMPLETED_ORDERS_NUMBER("Completed orders number: "),
    ENTER_CUSTOMER_DATA("Please, enter customer data: "),
    PROFIT("Profit: "),

    ENTER_REQUEST_ID("Please, enter request id: "),
    REQUEST_NOT_EXIST("Request not exist."),
    REQUEST_HAS_BEEN_CREATED("Book request has been created."),
    ENTER_REQUESTER_DATA("Please, enter requester data: "),

    EARLIEST_DATE_BOUND_YYYY_MM_DD("Please enter an earliest date bound in format \"yyyy-MM-dd\": "),
    LATEST_DATE_BOUND_YYYY_MM_DD("Please enter a latest date bound in format \"yyyy-MM-dd\": "),

    IMPORTED_ENTITIES("Imported entities number: "),
    EXPORTED_ENTITIES("Exported entities number: "),
    ENTITY_EXPORTED("Entity has been exported."),
    ENTER_FILE_NAME("Please, enter file name: "),
    CANT_PARSE_BOOK("Can't parse book from cvs file."),
    CANT_PARSE_ORDER("Can't parse order from cvs file."),
    CANT_PARSE_REQUEST("Can't parse request from cvs file."),
    ENTER_CORRECT_NUMBER("Please, enter the correct number.");

    private String message;

    MessageConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
