package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum LoggerConstant {

    REPLENISH_BOOK_SUCCESS("Book[id={}] has been replenished."),
    REPLENISH_BOOK_FAIL("Book[id={}] has not been replenished. Cause: {}"),
    WRITE_OFF_BOOK_SUCCESS("Book[id={}] has been written off."),
    WRITE_OFF_BOOK_FAIL("Book[id={}] has not been written off. Cause: {}"),
    FIND_ALL_BOOKS_SORTED_BY_AVAILABILITY("Books searched. Sorted by availability."),
    FIND_ALL_BOOKS_SORTED_BY_EDITION_YEAR("Books searched. Sorted by edition year."),
    FIND_ALL_BOOKS_SORTED_BY_PRICE("Books searched. Sorted by price."),
    FIND_ALL_BOOKS_SORTED_BY_REPLENISHMENT_DATE("Books searched. Sorted by replenishment date."),
    FIND_ALL_BOOKS_SORTED_BY_TITLE("Books searched. Sorted by title."),
    FIND_SOLD_BOOKS("Sold books between dates([{}] and [{}]) searched."),
    FIND_UNSOLD_BOOKS("Unsold books between dates([{}] and [{}]) searched."),
    FIND_STALE_BOOKS("Stale books searched"),
    SHOW_BOOK_DESCRIPTION_SUCCESS("Book[id={}] description was showed."),
    SHOW_BOOK_DESCRIPTION_FAIL("Book[id={}] description wasn't showed. Cause: {}"),
    IMPORT_BOOKS_SUCCESS("Books were imported. File name: {}"),
    IMPORT_BOOKS_FAIL("Books were not imported. Cause: {}"),
    EXPORT_ALL_BOOKS("Books were exported. File name: {}"),
    EXPORT_BOOK_SUCCESS("Book[id={}] was exported. File name: {}"),
    EXPORT_BOOK_FAIL("Book[id={}] wasn't exported. Cause: {}"),

    CREATE_ORDER_SUCCESS("Order[id={}] on Book[id={}] has been created."),
    CREATE_ORDER_FAIL("Order on Book[id={}] has not been created. Cause: {}"),
    CANCEL_ORDER_SUCCESS("Order[id={}] has been canceled."),
    CANCEL_ORDER_FAIL("Order[id={}] has not been canceled. Cause: {}"),
    COMPLETE_ORDER_SUCCESS("Order[id={}] has been completed."),
    COMPLETE_ORDER_FAIL("Order[id={}] has not been completed. Cause: {}"),
    FIND_ALL_ORDERS_SORTED_BY_COMPLETION_DATE("Orders searched. Sorted by completion date."),
    FIND_ALL_ORDERS_SORTED_BY_PRICE("Orders searched. Sorted by price."),
    FIND_ALL_ORDERS_SORTED_BY_STATE("Orders searched. Sorted by state."),
    FIND_COMPLETED_ORDERS_BETWEEN_DATES("Completed orders searched. Between dates [{}] and [{}]."),
    SHOW_PROFIT_BETWEEN_DATES("Profit showed. Between dates [{}] and [{}]."),
    SHOW_COMPLETE_ORDERS_NUMBER_BETWEEN_DATES("Completed orders number showed. Between dates [{}] and [{}]."),
    SHOW_ORDER_DETAILS_SUCCESS("Order[id={}] details showed."),
    SHOW_ORDER_DETAILS_FAIL("Order[id={}] details wasn't showed. Cause: {}"),
    IMPORT_ORDERS_SUCCESS("Orders were imported. File name: {}"),
    IMPORT_ORDERS_FAIL("Orders were not imported. Cause: {}"),
    EXPORT_ALL_ORDERS("Orders were exported. File name: {}"),
    EXPORT_ORDER_SUCCESS("Order[id={}] was exported. File name: {}"),
    EXPORT_ORDER_FAIL("Order[id={}] wasn't exported. Cause: {}"),

    CREATE_REQUEST_SUCCESS("Request on Book[id={}] has been created."),
    CREATE_REQUEST_FAIL("Request on Book[id={}] has not been created. Cause: {}"),
    REQUESTS_CLOSED("Requests on Book[id={}] has been closed."),
    FIND_ALL_REQUESTS_SORTED_BY_BOOK_TITLE("Requests searched. Sorted by book's title"),
    FIND_ALL_REQUESTS_SORTED_BY_IS_ACTIVE("Requests searched. Sorted by 'is active'"),
    FIND_ALL_REQUESTS_SORTED_BY_REQUESTER_DATA("Requests searched. Sorted by requester data"),
    IMPORT_REQUESTS_SUCCESS("Requests were imported. File name: {}"),
    IMPORT_REQUESTS_FAIL("Requests were not imported. Cause: {}"),
    EXPORT_ALL_REQUESTS("Requests were exported. File name: {}"),
    EXPORT_REQUEST_SUCCESS("Request[id={}] was exported. File name: {}"),
    EXPORT_REQUEST_FAIL("Requests[id={}] wasn't exported. Cause: {}"),

    SESSION_FACTORY_BUILD_FAILURE("Session factory build failure. Cause: {}");

    private String message;

    LoggerConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
