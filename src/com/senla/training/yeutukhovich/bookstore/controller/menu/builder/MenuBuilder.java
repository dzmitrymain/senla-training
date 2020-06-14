package com.senla.training.yeutukhovich.bookstore.controller.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.action.ActionType;
import com.senla.training.yeutukhovich.bookstore.controller.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.controller.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.util.constant.MenuNameConstant;

import java.util.Collections;

public class MenuBuilder {

    private Menu rootMenu;

    public void buildMenu() {
        rootMenu = new Menu(MenuNameConstant.MAIN_MENU);
        Menu bookMenu = new Menu(MenuNameConstant.BOOK_MENU);
        Menu orderMenu = new Menu(MenuNameConstant.ORDER_MENU);
        Menu requestMenu = new Menu(MenuNameConstant.REQUEST_MENU);

        MenuItem previousRootMenuItem = new MenuItem(MenuNameConstant.BACK_TO_MAIN_MENU, null, rootMenu);

        Menu showAllBooksMenu = new Menu(MenuNameConstant.SHOW_ALL_BOOKS);
        Menu showAllOrdersMenu = new Menu(MenuNameConstant.SHOW_ALL_ORDERS);
        Menu showAllRequestsMenu = new Menu(MenuNameConstant.SHOW_ALL_REQUESTS);

        MenuItem bookMenuItem = new MenuItem(MenuNameConstant.BOOK_MENU, null, bookMenu);
        MenuItem orderMenuItem = new MenuItem(MenuNameConstant.ORDER_MENU, null, orderMenu);
        MenuItem requestMenuItem = new MenuItem(MenuNameConstant.REQUEST_MENU, null, requestMenu);
        MenuItem exitMenuItem = new MenuItem(MenuNameConstant.EXIT, null, null);

        Collections.addAll(rootMenu.getMenuItems(), bookMenuItem, orderMenuItem, requestMenuItem, exitMenuItem);

        MenuItem allBooksSortByTitle = new MenuItem(MenuNameConstant.SORT_BY_TITLE,
                ActionType.SHOW_ALL_BOOKS_SORT_BY_TITLE.getAction(), showAllBooksMenu);
        MenuItem allBooksSortByPrice = new MenuItem(MenuNameConstant.SORT_BY_PRICE,
                ActionType.SHOW_ALL_BOOKS_SORT_BY_PRICE.getAction(), showAllBooksMenu);
        MenuItem allBooksSortByAvailability = new MenuItem(MenuNameConstant.SORT_BY_AVAILABILITY,
                ActionType.SHOW_ALL_BOOKS_SORT_BY_AVAILABILITY.getAction(), showAllBooksMenu);
        MenuItem allBooksSortByEditionDate = new MenuItem(MenuNameConstant.SORT_BY_EDITION_DATE,
                ActionType.SHOW_ALL_BOOKS_SORT_BY_EDITION_DATE.getAction(), showAllBooksMenu);
        MenuItem allBooksSortByReplenishmentDate = new MenuItem(MenuNameConstant.SORT_BY_REPLENISHMENT_DATE,
                ActionType.SHOW_ALL_BOOKS_SORT_BY_REPLENISHMENT_DATE.getAction(), showAllBooksMenu);
        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU, null, bookMenu);

        Collections.addAll(showAllBooksMenu.getMenuItems(), allBooksSortByTitle, allBooksSortByPrice,
                allBooksSortByAvailability, allBooksSortByEditionDate, allBooksSortByReplenishmentDate,
                previousBookMenuItem);

        MenuItem showAllBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_BOOKS, null, showAllBooksMenu);
        MenuItem showBookDescriptionItem = new MenuItem(MenuNameConstant.SHOW_BOOK_DESCRIPTION,
                ActionType.SHOW_BOOK_DESCRIPTION.getAction(), bookMenu);
        MenuItem replenishBookItem = new MenuItem(MenuNameConstant.REPLENISH_BOOK,
                ActionType.REPLENISH_BOOK.getAction(), bookMenu);
        MenuItem writeOffBookItem = new MenuItem(MenuNameConstant.WRITE_OFF_BOOK,
                ActionType.WRITE_OFF_BOOK.getAction(), bookMenu);
        MenuItem findStaleBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_STALE_BOOKS,
                ActionType.SHOW_STALE_BOOKS.getAction(), bookMenu);
        MenuItem findSoldBooksItem = new MenuItem(MenuNameConstant.SHOW_SOLD_BOOKS_BETWEEN_DATES,
                ActionType.SHOW_SOLD_BOOKS_BETWEEN_DATES.getAction(), bookMenu);
        MenuItem findUnsoldBooksItem = new MenuItem(MenuNameConstant.SHOW_UNSOLD_BOOKS_BETWEEN_DATES,
                ActionType.SHOW_UNSOLD_BOOKS_BETWEEN_DATES.getAction(), bookMenu);

        Collections.addAll(bookMenu.getMenuItems(), showAllBooksMenuItem, showBookDescriptionItem,
                replenishBookItem, writeOffBookItem, findStaleBooksMenuItem, findSoldBooksItem,
                findUnsoldBooksItem, previousRootMenuItem);

        MenuItem allOrdersByStateMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE,
                ActionType.SHOW_ALL_ORDERS_SORT_BY_STATE.getAction(), showAllOrdersMenu);
        MenuItem allOrdersByPriceMenuItem = new MenuItem(MenuNameConstant.SORT_BY_PRICE,
                ActionType.SHOW_ALL_ORDERS_SORT_BY_PRICE.getAction(), showAllOrdersMenu);
        MenuItem allOrdersByCompletionMenuItem = new MenuItem(MenuNameConstant.SORT_BY_COMPLETION_DATE,
                ActionType.SHOW_ALL_ORDERS_SORT_BY_COMPLETION_DATE.getAction(), showAllOrdersMenu);
        MenuItem previousOrderMenuItem = new MenuItem(MenuNameConstant.BACK_TO_ORDER_MENU, null, orderMenu);

        Collections.addAll(showAllOrdersMenu.getMenuItems(), allOrdersByStateMenuItem, allOrdersByPriceMenuItem,
                allOrdersByCompletionMenuItem, previousOrderMenuItem);

        MenuItem showAllOrdersMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_ORDERS, null, showAllOrdersMenu);
        MenuItem cancelOrderMenuItem = new MenuItem(MenuNameConstant.CANCEL_ORDER, ActionType.CANCEL_ORDER.getAction(),
                orderMenu);
        MenuItem createOrderMenuItem = new MenuItem(MenuNameConstant.CREATE_ORDER, ActionType.CREATE_ORDER.getAction(),
                orderMenu);
        MenuItem completeOrderMenuItem = new MenuItem(MenuNameConstant.COMPLETE_ORDER,
                ActionType.COMPLETE_ORDER.getAction(), orderMenu);
        MenuItem showCompletedOrdersNumberMenuItem = new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES,
                ActionType.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES.getAction(), orderMenu);
        MenuItem showCompletedOrdersMenuItem = new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_BETWEEN_DATES,
                ActionType.SHOW_COMPLETED_ORDERS_BETWEEN_DATES.getAction(), orderMenu);
        MenuItem showOrderDetailsMenuItem = new MenuItem(MenuNameConstant.SHOW_ORDER_DETAILS,
                ActionType.SHOW_ORDER_DETAILS.getAction(), orderMenu);
        MenuItem showProfitBetweenDatesMenuItem = new MenuItem(MenuNameConstant.SHOW_PROFIT_BETWEEN_DATES,
                ActionType.SHOW_PROFIT_BETWEEN_DATES.getAction(), orderMenu);

        Collections.addAll(orderMenu.getMenuItems(), showAllOrdersMenuItem, createOrderMenuItem, completeOrderMenuItem,
                cancelOrderMenuItem, showOrderDetailsMenuItem, showProfitBetweenDatesMenuItem,
                showCompletedOrdersMenuItem, showCompletedOrdersNumberMenuItem, previousRootMenuItem);

        MenuItem allRequestsSortByBookTitleMenuItem = new MenuItem(MenuNameConstant.SORT_BY_BOOK_TITLE,
                ActionType.SHOW_ALL_REQUESTS_SORT_BY_BOOK_TITLE.getAction(), showAllRequestsMenu);
        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE,
                ActionType.SHOW_ALL_REQUESTS_SORT_BY_IS_ACTIVE.getAction(), showAllRequestsMenu);
        MenuItem allRequestsSortByRequesterDataMenuItem = new MenuItem(MenuNameConstant.SORT_BY_REQUESTER_DATA,
                ActionType.SHOW_ALL_REQUESTS_SORT_BY_REQUESTER_DATA.getAction(), showAllRequestsMenu);
        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU, null, requestMenu);

        Collections.addAll(showAllRequestsMenu.getMenuItems(), allRequestsSortByBookTitleMenuItem,
                allRequestsSortByIsActiveMenuItem, allRequestsSortByRequesterDataMenuItem, previousRequestMenuItem);

        MenuItem showAllRequestsMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_REQUESTS, null, showAllRequestsMenu);
        MenuItem createRequestMenuItem = new MenuItem(MenuNameConstant.CREATE_REQUEST,
                ActionType.CREATE_REQUEST.getAction(), requestMenu);

        Collections.addAll(requestMenu.getMenuItems(), showAllRequestsMenuItem, createRequestMenuItem, previousRootMenuItem);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}
