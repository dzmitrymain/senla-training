package com.senla.training.yeutukhovich.bookstore.controller.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.action.ActionType;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.*;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.ShowAllBooksSortByEditionAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.ShowAllBooksSortByPriceAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.ShowAllBooksSortByReplenishmentAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.ShowAllBooksSortByTitleAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.*;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.showallorders.ShowAllOrdersSortByCompletionDateAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.showallorders.ShowAllOrdersSortByPriceAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.orderaction.showallorders.ShowAllOrdersSortByStateAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction.CreateRequestAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction.showallrequests.ShowAllRequestsSortByBookTitleAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction.showallrequests.ShowAllRequestsSortByIsActiveAction;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.requestaction.showallrequests.ShowAllRequestsSortByRequesterDataAction;
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

        MenuItem allBooksSortByTitle = new MenuItem(MenuNameConstant.SORT_BY_TITLE, new ShowAllBooksSortByTitleAction(),
                showAllBooksMenu);
        MenuItem allBooksSortByPrice = new MenuItem(MenuNameConstant.SORT_BY_PRICE, new ShowAllBooksSortByPriceAction(),
                showAllBooksMenu);
        MenuItem allBooksSortByAvailability = new MenuItem(MenuNameConstant.SORT_BY_AVAILABILITY, ActionType.SHOW_ALL_BOOKS_SORT_BY_AVAILABILITY.getAction(), showAllBooksMenu);
        MenuItem allBooksSortByEditionDate = new MenuItem(MenuNameConstant.SORT_BY_EDITION_DATE,
                new ShowAllBooksSortByEditionAction(), showAllBooksMenu);
        MenuItem allBooksSortByReplenishmentDate = new MenuItem(MenuNameConstant.SORT_BY_REPLENISHMENT_DATE,
                new ShowAllBooksSortByReplenishmentAction(), showAllBooksMenu);
        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU, null, bookMenu);

        Collections.addAll(showAllBooksMenu.getMenuItems(), allBooksSortByTitle, allBooksSortByPrice,
                allBooksSortByAvailability,
                allBooksSortByEditionDate, allBooksSortByReplenishmentDate, previousBookMenuItem);

        MenuItem showAllBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_BOOKS, null, showAllBooksMenu);
        MenuItem showBookDescriptionItem = new MenuItem(MenuNameConstant.SHOW_BOOK_DESCRIPTION, new ShowBookDescriptionAction(),
                bookMenu);
        MenuItem replenishBookItem = new MenuItem(MenuNameConstant.REPLENISH_BOOK, new ReplenishBookAction(), bookMenu);
        MenuItem writeOffBookItem = new MenuItem(MenuNameConstant.WRITE_OFF_BOOK, new WriteOffBookAction(), bookMenu);
        MenuItem findStaleBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_STALE_BOOKS, new FindStaleBooksAction(), bookMenu);
        MenuItem findSoldBooksItem = new MenuItem(MenuNameConstant.SHOW_SOLD_BOOKS_BETWEEN_DATES,
                new FindSoldBooksBetweenDatesAction(), bookMenu);
        MenuItem findUnsoldBooksItem = new MenuItem(MenuNameConstant.SHOW_UNSOLD_BOOKS_BETWEEN_DATES,
                new FindUnsoldBooksBetweenDatesAction(), bookMenu);

        Collections.addAll(bookMenu.getMenuItems(), showAllBooksMenuItem, showBookDescriptionItem, replenishBookItem,
                writeOffBookItem, findStaleBooksMenuItem, findSoldBooksItem, findUnsoldBooksItem, previousRootMenuItem);

        MenuItem allOrdersByStateMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE, new ShowAllOrdersSortByStateAction(),
                showAllOrdersMenu);
        MenuItem allOrdersByPriceMenuItem = new MenuItem(MenuNameConstant.SORT_BY_PRICE, new ShowAllOrdersSortByPriceAction(),
                showAllOrdersMenu);
        MenuItem allOrdersByCompletionMenuItem = new MenuItem(MenuNameConstant.SORT_BY_COMPLETION_DATE,
                new ShowAllOrdersSortByCompletionDateAction(), showAllOrdersMenu);
        MenuItem previousOrderMenuItem = new MenuItem(MenuNameConstant.BACK_TO_ORDER_MENU, null, orderMenu);

        Collections.addAll(showAllOrdersMenu.getMenuItems(), allOrdersByStateMenuItem, allOrdersByPriceMenuItem,
                allOrdersByCompletionMenuItem, previousOrderMenuItem);

        MenuItem showAllOrdersMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_ORDERS, null, showAllOrdersMenu);
        MenuItem cancelOrderMenuItem = new MenuItem(MenuNameConstant.CANCEL_ORDER, new CancelOrderAction(), orderMenu);
        MenuItem createOrderMenuItem = new MenuItem(MenuNameConstant.CREATE_ORDER, new CreateOrderAction(), orderMenu);
        MenuItem completeOrderMenuItem = new MenuItem(MenuNameConstant.COMPLETE_ORDER, new CompleteOrderAction(), orderMenu);
        MenuItem showCompletedOrdersNumberMenuItem = new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES,
                new ShowCompletedOrdersNumberBetweenDatesAction(), orderMenu);
        MenuItem showCompletedOrdersMenuItem = new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_BETWEEN_DATES,
                new ShowCompletedOrdersBetweenDatesAction(), orderMenu);
        MenuItem showOrderDetailsMenuItem = new MenuItem(MenuNameConstant.SHOW_ORDER_DETAILS, new ShowOrderDetailsAction(),
                orderMenu);
        MenuItem showProfitBetweenDatesMenuItem = new MenuItem(MenuNameConstant.SHOW_PROFIT_BETWEEN_DATES,
                new ShowProfitBetweenDatesAction(), orderMenu);

        Collections.addAll(orderMenu.getMenuItems(), showAllOrdersMenuItem, createOrderMenuItem, completeOrderMenuItem,
                cancelOrderMenuItem, showOrderDetailsMenuItem,
                showProfitBetweenDatesMenuItem, showCompletedOrdersMenuItem, showCompletedOrdersNumberMenuItem,
                previousRootMenuItem);

        MenuItem allRequestsSortByBookTitleMenuItem = new MenuItem(MenuNameConstant.SORT_BY_BOOK_TITLE,
                new ShowAllRequestsSortByBookTitleAction(), showAllRequestsMenu);
        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE,
                new ShowAllRequestsSortByIsActiveAction(), showAllRequestsMenu);
        MenuItem allRequestsSortByRequesterDataMenuItem = new MenuItem(MenuNameConstant.SORT_BY_REQUESTER_DATA,
                new ShowAllRequestsSortByRequesterDataAction(), showAllRequestsMenu);
        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU, null, requestMenu);

        Collections.addAll(showAllRequestsMenu.getMenuItems(), allRequestsSortByBookTitleMenuItem,
                allRequestsSortByIsActiveMenuItem, allRequestsSortByRequesterDataMenuItem, previousRequestMenuItem);

        MenuItem showAllRequestsMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_REQUESTS, null, showAllRequestsMenu);
        MenuItem createRequestMenuItem = new MenuItem(MenuNameConstant.CREATE_REQUEST, new CreateRequestAction(), requestMenu);

        Collections.addAll(requestMenu.getMenuItems(), showAllRequestsMenuItem, createRequestMenuItem,
                previousRootMenuItem);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }
}
