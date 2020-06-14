package com.senla.training.yeutukhovich.bookstore.controller.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.*;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.*;
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

public class MenuBuilder {

    private Menu rootMenu;

    public void buildMenu() {

        rootMenu = new Menu();
        rootMenu.setName("Main menu");

        Menu bookMenu = new Menu();
        bookMenu.setName("Book menu");

        Menu orderMenu = new Menu();
        orderMenu.setName("Order menu");

        Menu requestMenu = new Menu();
        requestMenu.setName("Request menu");

        MenuItem bookMenuItem = new MenuItem();
        bookMenuItem.setTitle("Book menu");
        bookMenuItem.setNextMenu(bookMenu);
        rootMenu.getMenuItems().add(bookMenuItem);

        MenuItem orderMenuItem = new MenuItem();
        orderMenuItem.setTitle("Order menu");
        orderMenuItem.setNextMenu(orderMenu);
        rootMenu.getMenuItems().add(orderMenuItem);

        MenuItem requestMenuItem = new MenuItem();
        requestMenuItem.setTitle("Request menu");
        requestMenuItem.setNextMenu(requestMenu);
        rootMenu.getMenuItems().add(requestMenuItem);

        MenuItem exitMenuItem = new MenuItem();
        exitMenuItem.setTitle("Exit");
        rootMenu.getMenuItems().add(exitMenuItem);


        MenuItem previousRootMenuItem = new MenuItem();
        previousRootMenuItem.setTitle("Back to Main menu");
        previousRootMenuItem.setNextMenu(rootMenu);


        ////////////////////////////////
        Menu showAllBooksMenu = new Menu();
        showAllBooksMenu.setName("Show all books");

        MenuItem allBooksSortByTitle = new MenuItem();
        allBooksSortByTitle.setTitle("Sort by title");
        allBooksSortByTitle.setAction(new ShowAllBooksSortByTitleAction());
        allBooksSortByTitle.setNextMenu(showAllBooksMenu);
        showAllBooksMenu.getMenuItems().add(allBooksSortByTitle);

        MenuItem allBooksSortByPrice = new MenuItem();
        allBooksSortByPrice.setTitle("Sort by price");
        allBooksSortByPrice.setAction(new ShowAllBooksSortByPriceAction());
        allBooksSortByPrice.setNextMenu(showAllBooksMenu);
        showAllBooksMenu.getMenuItems().add(allBooksSortByPrice);

        MenuItem allBooksSortByAvailability = new MenuItem();
        allBooksSortByAvailability.setTitle("Sort by availability");
        allBooksSortByAvailability.setAction(new ShowAllBooksSortByAvailabilityAction());
        allBooksSortByAvailability.setNextMenu(showAllBooksMenu);
        showAllBooksMenu.getMenuItems().add(allBooksSortByAvailability);

        MenuItem allBooksSortByEditionDate = new MenuItem();
        allBooksSortByEditionDate.setTitle("Sort by edition date");
        allBooksSortByEditionDate.setAction(new ShowAllBooksSortByEditionAction());
        allBooksSortByEditionDate.setNextMenu(showAllBooksMenu);
        showAllBooksMenu.getMenuItems().add(allBooksSortByEditionDate);

        MenuItem allBooksSortByReplenishmentDate = new MenuItem();
        allBooksSortByReplenishmentDate.setTitle("Sort by replenishment date");
        allBooksSortByReplenishmentDate.setAction(new ShowAllBooksSortByReplenishmentAction());
        allBooksSortByReplenishmentDate.setNextMenu(showAllBooksMenu);
        showAllBooksMenu.getMenuItems().add(allBooksSortByReplenishmentDate);

        MenuItem previousBookMenuItem = new MenuItem();
        previousBookMenuItem.setTitle("Back to Book menu");
        previousBookMenuItem.setNextMenu(bookMenu);
        showAllBooksMenu.getMenuItems().add(previousBookMenuItem);
        /////////////////////////////////////

        MenuItem showAllBooksMenuItem = new MenuItem();
        showAllBooksMenuItem.setTitle("Show all books");
        showAllBooksMenuItem.setNextMenu(showAllBooksMenu);

        MenuItem showBookDescriptionItem = new MenuItem();
        showBookDescriptionItem.setTitle("Show book description");
        showBookDescriptionItem.setNextMenu(bookMenu);
        showBookDescriptionItem.setAction(new ShowBookDescriptionAction());

        MenuItem replenishBookItem = new MenuItem();
        replenishBookItem.setTitle("Replenish book");
        replenishBookItem.setNextMenu(bookMenu);
        replenishBookItem.setAction(new ReplenishBookAction());

        MenuItem writeOffBookItem = new MenuItem();
        writeOffBookItem.setTitle("Write off book");
        writeOffBookItem.setNextMenu(bookMenu);
        writeOffBookItem.setAction(new WriteOffBookAction());

        MenuItem findStaleBooksMenuItem = new MenuItem();
        findStaleBooksMenuItem.setTitle("Show stale books");
        findStaleBooksMenuItem.setAction(new FindStaleBooksAction());
        findStaleBooksMenuItem.setNextMenu(bookMenu);

        MenuItem findSoldBooksItem = new MenuItem();
        findSoldBooksItem.setTitle("Show sold books between dates");
        findSoldBooksItem.setAction(new FindSoldBooksBetweenDatesAction());
        findSoldBooksItem.setNextMenu(bookMenu);

        MenuItem findUnsoldBooksItem = new MenuItem();
        findUnsoldBooksItem.setTitle("Show unsold books between dates");
        findUnsoldBooksItem.setAction(new FindUnsoldBooksBetweenDatesAction());
        findUnsoldBooksItem.setNextMenu(bookMenu);


        bookMenu.getMenuItems().add(showAllBooksMenuItem);
        bookMenu.getMenuItems().add(showBookDescriptionItem);
        bookMenu.getMenuItems().add(replenishBookItem);
        bookMenu.getMenuItems().add(writeOffBookItem);
        bookMenu.getMenuItems().add(findStaleBooksMenuItem);
        bookMenu.getMenuItems().add(findSoldBooksItem);
        bookMenu.getMenuItems().add(findUnsoldBooksItem);
        bookMenu.getMenuItems().add(previousRootMenuItem);

        ///////////////////////////////
        Menu showAllOrdersMenu = new Menu();
        showAllOrdersMenu.setName("Show all orders");

        MenuItem allOrdersByStateMenuItem = new MenuItem();
        allOrdersByStateMenuItem.setTitle("Sort by state");
        allOrdersByStateMenuItem.setAction(new ShowAllOrdersSortByStateAction());
        allOrdersByStateMenuItem.setNextMenu(showAllOrdersMenu);
        showAllOrdersMenu.getMenuItems().add(allOrdersByStateMenuItem);

        MenuItem allOrdersByPriceMenuItem = new MenuItem();
        allOrdersByPriceMenuItem.setTitle("Sort by price");
        allOrdersByPriceMenuItem.setAction(new ShowAllOrdersSortByPriceAction());
        allOrdersByPriceMenuItem.setNextMenu(showAllOrdersMenu);
        showAllOrdersMenu.getMenuItems().add(allOrdersByPriceMenuItem);

        MenuItem allOrdersByCompletionMenuItem = new MenuItem();
        allOrdersByCompletionMenuItem.setTitle("Sort by completion date");
        allOrdersByCompletionMenuItem.setAction(new ShowAllOrdersSortByCompletionDateAction());
        allOrdersByCompletionMenuItem.setNextMenu(showAllOrdersMenu);
        showAllOrdersMenu.getMenuItems().add(allOrdersByCompletionMenuItem);

        MenuItem previousOrderMenuItem = new MenuItem();
        previousOrderMenuItem.setTitle("Back to Order menu");
        previousOrderMenuItem.setNextMenu(orderMenu);
        showAllOrdersMenu.getMenuItems().add(previousOrderMenuItem);
        //////////////////////////////

        MenuItem showAllOrdersMenuItem = new MenuItem();
        showAllOrdersMenuItem.setTitle("Show all orders");
        showAllOrdersMenuItem.setNextMenu(showAllOrdersMenu);

        MenuItem cancelOrderMenuItem = new MenuItem();
        cancelOrderMenuItem.setTitle("Cancel order");
        cancelOrderMenuItem.setAction(new CancelOrderAction());
        cancelOrderMenuItem.setNextMenu(orderMenu);

        MenuItem createOrderMenuItem = new MenuItem();
        createOrderMenuItem.setTitle("Create order");
        createOrderMenuItem.setAction(new CreateOrderAction());
        createOrderMenuItem.setNextMenu(orderMenu);

        MenuItem completeOrderMenuItem = new MenuItem();
        completeOrderMenuItem.setTitle("Complete order");
        completeOrderMenuItem.setAction(new CompleteOrderAction());
        completeOrderMenuItem.setNextMenu(orderMenu);

        MenuItem showCompletedOrdersNumberMenuItem = new MenuItem();
        showCompletedOrdersNumberMenuItem.setTitle("Show completed orders number between dates");
        showCompletedOrdersNumberMenuItem.setAction(new ShowCompletedOrdersNumberBetweenDatesAction());
        showCompletedOrdersNumberMenuItem.setNextMenu(orderMenu);

        MenuItem showCompletedOrdersMenuItem = new MenuItem();
        showCompletedOrdersMenuItem.setTitle("Show completed orders between dates");
        showCompletedOrdersMenuItem.setAction(new ShowCompletedOrdersBetweenDatesAction());
        showCompletedOrdersMenuItem.setNextMenu(orderMenu);

        MenuItem showOrderDetailsMenuItem = new MenuItem();
        showOrderDetailsMenuItem.setTitle("Show order details");
        showOrderDetailsMenuItem.setAction(new ShowOrderDetailsAction());
        showOrderDetailsMenuItem.setNextMenu(orderMenu);

        MenuItem showProfitBetweenDatesMenuItem = new MenuItem();
        showProfitBetweenDatesMenuItem.setTitle("Show profit between dates");
        showProfitBetweenDatesMenuItem.setAction(new ShowProfitBetweenDatesAction());
        showProfitBetweenDatesMenuItem.setNextMenu(orderMenu);

        orderMenu.getMenuItems().add(showAllOrdersMenuItem);
        orderMenu.getMenuItems().add(createOrderMenuItem);
        orderMenu.getMenuItems().add(completeOrderMenuItem);
        orderMenu.getMenuItems().add(cancelOrderMenuItem);
        orderMenu.getMenuItems().add(showOrderDetailsMenuItem);
        orderMenu.getMenuItems().add(showProfitBetweenDatesMenuItem);
        orderMenu.getMenuItems().add(showCompletedOrdersMenuItem);
        orderMenu.getMenuItems().add(showCompletedOrdersNumberMenuItem);
        orderMenu.getMenuItems().add(previousRootMenuItem);

        ///////////////////////////////
        Menu showAllRequestsMenu = new Menu();
        showAllRequestsMenu.setName("Show all requests");

        MenuItem allRequestsSortByBookTitleMenuItem = new MenuItem();
        allRequestsSortByBookTitleMenuItem.setTitle("Sort by book title");
        allRequestsSortByBookTitleMenuItem.setAction(new ShowAllRequestsSortByBookTitleAction());
        allRequestsSortByBookTitleMenuItem.setNextMenu(showAllRequestsMenu);
        showAllRequestsMenu.getMenuItems().add(allRequestsSortByBookTitleMenuItem);

        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem();
        allRequestsSortByIsActiveMenuItem.setTitle("Sort by state");
        allRequestsSortByIsActiveMenuItem.setAction(new ShowAllRequestsSortByIsActiveAction());
        allRequestsSortByIsActiveMenuItem.setNextMenu(showAllRequestsMenu);
        showAllRequestsMenu.getMenuItems().add(allRequestsSortByIsActiveMenuItem);

        MenuItem allRequestsSortByRequesterDataMenuItem = new MenuItem();
        allRequestsSortByRequesterDataMenuItem.setTitle("Sort by requester data");
        allRequestsSortByRequesterDataMenuItem.setAction(new ShowAllRequestsSortByRequesterDataAction());
        allRequestsSortByRequesterDataMenuItem.setNextMenu(showAllRequestsMenu);
        showAllRequestsMenu.getMenuItems().add(allRequestsSortByRequesterDataMenuItem);

        MenuItem previousRequestMenuItem = new MenuItem();
        previousRequestMenuItem.setTitle("Back to Request menu");
        previousRequestMenuItem.setNextMenu(requestMenu);
        showAllRequestsMenu.getMenuItems().add(previousRequestMenuItem);
        //////////////////////////////

        MenuItem showAllRequestsMenuItem = new MenuItem();
        showAllRequestsMenuItem.setTitle("Show all requests");
        showAllRequestsMenuItem.setNextMenu(showAllRequestsMenu);

        MenuItem createRequestMenuItem = new MenuItem();
        createRequestMenuItem.setTitle("Create request");
        createRequestMenuItem.setAction(new CreateRequestAction());
        createRequestMenuItem.setNextMenu(requestMenu);

        requestMenu.getMenuItems().add(showAllRequestsMenuItem);
        requestMenu.getMenuItems().add(createRequestMenuItem);
        requestMenu.getMenuItems().add(previousRootMenuItem);
    }

    public Menu getRootMenu() {
        return rootMenu;
    }

}
