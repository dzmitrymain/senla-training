package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum MenuNameConstant {

    AUTHENTICATION("Authentication"),
    REGISTRATION("Registration"),
    MAIN_MENU("Main menu"),
    BOOK_MENU("Book menu"),
    ORDER_MENU("Order menu"),
    REQUEST_MENU("Request menu"),
    EXIT("Exit"),

    BACK_TO_MAIN_MENU("Back to Main menu"),
    BACK_TO_BOOK_MENU("Back to Book menu"),
    BACK_TO_ORDER_MENU("Back to Order menu"),
    BACK_TO_REQUEST_MENU("Back to Request menu"),

    SHOW_ALL_BOOKS("Show all books"),
    SHOW_ALL_ORDERS("Show all orders"),
    SHOW_ALL_REQUESTS("Show all requests"),

    SORT_BY_TITLE("Sort by title"),
    SORT_BY_BOOK_TITLE("Sort by book title"),
    SORT_BY_STATE("Sort by state"),
    SORT_BY_PRICE("Sort by price"),
    SORT_BY_AVAILABILITY("Sort by availability"),
    SORT_BY_EDITION_YEAR("Sort by edition year"),
    SORT_BY_REPLENISHMENT_DATE("Sort by replenishment date"),
    SORT_BY_COMPLETION_DATE("Sort by completion date"),
    SORT_BY_REQUESTER_DATA("Sort by requester data"),

    SHOW_BOOK_DESCRIPTION("Show book description"),
    REPLENISH_BOOK("Replenish book"),
    WRITE_OFF_BOOK("Write off book"),
    SHOW_STALE_BOOKS("Show stale books"),
    SHOW_SOLD_BOOKS_BETWEEN_DATES("Show sold books between dates"),
    SHOW_UNSOLD_BOOKS_BETWEEN_DATES("Show unsold books between dates"),
    IMPORT_BOOKS("Import books"),
    EXPORT_BOOKS("Export books"),
    EXPORT_BOOK_BY_ID("Export book by id"),
    EXPORT_ALL_BOOKS("Export all books"),

    CANCEL_ORDER("Cancel order"),
    COMPLETE_ORDER("Complete order"),
    CREATE_ORDER("Create order"),
    SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES("Show completed orders number between dates"),
    SHOW_COMPLETED_ORDERS_BETWEEN_DATES("Show completed orders between dates"),
    SHOW_ORDER_DETAILS("Show order details"),
    SHOW_PROFIT_BETWEEN_DATES("Show profit between dates"),
    IMPORT_ORDERS("Import orders"),
    EXPORT_ORDERS("Export orders"),
    EXPORT_ORDER_BY_ID("Export order by id"),
    EXPORT_ALL_ORDERS("Export all orders"),

    IMPORT_REQUESTS("Import requests"),
    EXPORT_REQUESTS("Export requests"),
    EXPORT_REQUEST_BY_ID("Export request by id"),
    EXPORT_ALL_REQUESTS("Export all requests"),
    CREATE_REQUEST("Create request");

    private String menuName;

    MenuNameConstant(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }
}
