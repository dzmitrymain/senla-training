package com.senla.training.yeutukhovich.bookstore.ui.action;

import com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.*;
import com.senla.training.yeutukhovich.bookstore.ui.action.bookaction.showallbooks.*;
import com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.*;
import com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders.ShowAllOrdersSortByCompletionDateAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders.ShowAllOrdersSortByPriceAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.orderaction.showallorders.ShowAllOrdersSortByStateAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.CreateRequestAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.ExportAllRequestsAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.ExportRequestAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.ImportRequestsAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.showallrequests.ShowAllRequestsSortByBookTitleAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.showallrequests.ShowAllRequestsSortByIsActiveAction;
import com.senla.training.yeutukhovich.bookstore.ui.action.requestaction.showallrequests.ShowAllRequestsSortByRequesterDataAction;

public enum ActionType {

    SHOW_ALL_BOOKS_SORT_BY_AVAILABILITY(new ShowAllBooksSortByAvailabilityAction()),
    SHOW_ALL_BOOKS_SORT_BY_EDITION_DATE(new ShowAllBooksSortByEditionAction()),
    SHOW_ALL_BOOKS_SORT_BY_PRICE(new ShowAllBooksSortByPriceAction()),
    SHOW_ALL_BOOKS_SORT_BY_REPLENISHMENT_DATE(new ShowAllBooksSortByReplenishmentAction()),
    SHOW_ALL_BOOKS_SORT_BY_TITLE(new ShowAllBooksSortByTitleAction()),

    SHOW_SOLD_BOOKS_BETWEEN_DATES(new ShowSoldBooksBetweenDatesAction()),
    SHOW_STALE_BOOKS(new ShowStaleBooksAction()),
    SHOW_UNSOLD_BOOKS_BETWEEN_DATES(new ShowUnsoldBooksBetweenDatesAction()),
    REPLENISH_BOOK(new ReplenishBookAction()),
    SHOW_BOOK_DESCRIPTION(new ShowBookDescriptionAction()),
    WRITE_OFF_BOOK(new WriteOffBookAction()),
    IMPORT_BOOKS(new ImportBooksAction()),
    EXPORT_BOOK(new ExportBookAction()),
    EXPORT_ALL_BOOKS(new ExportAllBooksAction()),

    SHOW_ALL_ORDERS_SORT_BY_COMPLETION_DATE(new ShowAllOrdersSortByCompletionDateAction()),
    SHOW_ALL_ORDERS_SORT_BY_PRICE(new ShowAllOrdersSortByPriceAction()),
    SHOW_ALL_ORDERS_SORT_BY_STATE(new ShowAllOrdersSortByStateAction()),

    CANCEL_ORDER(new CancelOrderAction()),
    COMPLETE_ORDER(new CompleteOrderAction()),
    CREATE_ORDER(new CreateOrderAction()),
    SHOW_COMPLETED_ORDERS_BETWEEN_DATES(new ShowCompletedOrdersBetweenDatesAction()),
    SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES(new ShowCompletedOrdersNumberBetweenDatesAction()),
    SHOW_ORDER_DETAILS(new ShowOrderDetailsAction()),
    SHOW_PROFIT_BETWEEN_DATES(new ShowProfitBetweenDatesAction()),
    IMPORT_ORDERS(new ImportOrdersAction()),
    EXPORT_ORDER(new ExportOrderAction()),
    EXPORT_ALL_ORDERS(new ExportAllOrdersAction()),

    SHOW_ALL_REQUESTS_SORT_BY_BOOK_TITLE(new ShowAllRequestsSortByBookTitleAction()),
    SHOW_ALL_REQUESTS_SORT_BY_IS_ACTIVE(new ShowAllRequestsSortByIsActiveAction()),
    SHOW_ALL_REQUESTS_SORT_BY_REQUESTER_DATA(new ShowAllRequestsSortByRequesterDataAction()),

    IMPORT_REQUESTS(new ImportRequestsAction()),
    EXPORT_REQUEST(new ExportRequestAction()),
    EXPORT_ALL_REQUESTS(new ExportAllRequestsAction()),
    CREATE_REQUEST(new CreateRequestAction());

    private Action action;

    ActionType(Action action) {
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
