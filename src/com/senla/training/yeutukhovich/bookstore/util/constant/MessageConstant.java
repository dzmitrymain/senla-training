package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum MessageConstant {

    MENU_BORDER("========================================"),
    MENU_ITEM_BORDER("----------------------------------------"),

    DATA_WAS_FOUND("Data was found: "),
    NO_DATA_WAS_FOUND("No data was found."),

    ENTER_BOOK_ID("Please, enter book id: "),
    BOOK_HAS_BEEN_REPLENISHED("Book has been replenished. All requests has been closed."),
    BOOK_HAS_NOT_BEEN_REPLENISHED("Book has not been replenished. Check entered book id."),
    BOOK_HAS_BEEN_WRITTEN_OFF("Book has been written off."),
    BOOK_HAS_NOT_BEEN_WRITTEN_OFF("Book has not been written off. Check entered book id."),
    BOOK_DESCRIPTION_WAS_NOT_FOUND("Book description was not found. Check entered book id."),

    ENTER_ORDER_ID("Please, enter order id: "),
    ORDER_HAS_BEEN_CREATED("Order has been created."),
    ORDER_HAS_NOT_BEEN_CREATED("Order has not been created. Check entered book id."),
    ORDER_HAS_BEEN_CANCELED("Order has been canceled."),
    ORDER_HAS_NOT_BEEN_CANCELED("Order has not been canceled. Check entered order id."),
    ORDER_HAS_BEEN_COMPLETED("Order has been completed."),
    ORDER_HAS_NOT_BEEN_COMPLETED("Order has not been completed. Check entered order id."),
    COMPLETED_ORDERS_NUMBER("Completed orders number: "),
    ENTER_CUSTOMER_DATA("Please, enter customer data: "),
    ORDER_DETAILS_WAS_NOT_FOUND("Order details was not found. Check entered order id."),
    PROFIT("Profit: "),

    REQUEST_HAS_BEEN_CREATED("Book request has been created."),
    ENTER_REQUESTER_DATA("Please, enter requester data: "),
    REQUEST_HAS_NOT_BEEN_CREATED("Book request has not been created. Check entered book id."),

    EARLIEST_DATE_BOUND_YYYY_MM_DD("Please enter an earliest date bound in format \"yyyy-MM-dd\": "),
    LATEST_DATE_BOUND_YYYY_MM_DD("Please enter a latest date bound in format \"yyyy-MM-dd\": "),

    ENTER_CORRECT_NUMBER("Please, enter the correct number.");

    private String message;

    MessageConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
