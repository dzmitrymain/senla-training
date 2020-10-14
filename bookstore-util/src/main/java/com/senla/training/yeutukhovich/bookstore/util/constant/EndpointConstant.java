package com.senla.training.yeutukhovich.bookstore.util.constant;

public enum EndpointConstant {

    BOOKS_DESCRIPTION("/books/%d/description"),
    BOOKS_UPDATE("/books/%d"),
    BOOKS_STALE("/books/stale"),
    BOOKS_SOLD_BETWEEN_DATES("/books/soldBetweenDates?startDate=%s&endDate=%s"),
    BOOKS_UNSOLD_BETWEEN_DATES("/books/unsoldBetweenDates?startDate=%s&endDate=%s"),
    BOOKS_IMPORT("/books/import"),
    BOOKS_EXPORT("/books/%d/export"),
    BOOKS_EXPORT_ALL("/books/export"),
    BOOKS_BY_AVAILABILITY("/books?sort=AVAILABILITY"),
    BOOKS_BY_PRICE("/books?sort=PRICE"),
    BOOKS_BY_TITLE("/books?sort=TITLE"),
    BOOKS_BY_EDITION_YEAR("/books?sort=EDITION"),
    BOOKS_BY_REPLENISHMENT("/books?sort=REPLENISHMENT"),

    ORDERS_UPDATE_STATE("/orders/%d/state"),
    ORDERS_CREATE("/orders"),
    ORDERS_NUMBER_BETWEEN_DATES("/orders/ordersNumberBetweenDates?startDate=%s&endDate=%s"),
    ORDERS_COMPLETED_BETWEEN_DATES("/orders/completedBetweenDates?startDate=%s&endDate=%s"),
    ORDERS_DETAILS("/orders/%d/details"),
    ORDERS_PROFIT_BETWEEN_DATES("/orders/profitBetweenDates?startDate=%s&endDate=%s"),
    ORDERS_IMPORT("/orders/import"),
    ORDERS_EXPORT_ALL("/orders/export"),
    ORDERS_EXPORT("/orders/%d/export"),
    ORDERS_BY_STATE("/orders?sort=STATE"),
    ORDERS_BY_PRICE("/orders?sort=PRICE"),
    ORDERS_BY_COMPLETION_DATE("/orders?sort=COMPLETION"),

    REQUESTS_CREATE("/requests"),
    REQUESTS_IMPORT("/requests/import"),
    REQUESTS_EXPORT_ALL("/requests/export"),
    REQUESTS_EXPORT("/requests/%d/export"),
    REQUESTS_BY_BOOK_TITLE("/requests?sort=TITLE"),
    REQUESTS_BY_STATE("/requests?sort=STATE"),
    REQUESTS_BY_REQUESTER_DATA("/requests?sort=REQUESTER");

    private String endpoint;

    EndpointConstant(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }
}
