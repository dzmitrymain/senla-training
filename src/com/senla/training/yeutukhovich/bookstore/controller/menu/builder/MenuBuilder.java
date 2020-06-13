package com.senla.training.yeutukhovich.bookstore.controller.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.*;
import com.senla.training.yeutukhovich.bookstore.controller.action.impl.bookaction.showallbooks.*;
import com.senla.training.yeutukhovich.bookstore.controller.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.controller.menu.MenuItem;

public class MenuBuilder {

    private Menu rootMenu;

    public void buildMenu() {

        rootMenu = new Menu();
        rootMenu.setName("Main menu");

        MenuItem bookMenuItem = new MenuItem();
        bookMenuItem.setTitle("Book menu");
        rootMenu.getMenuItems().add(bookMenuItem);

        MenuItem exitMenuItem = new MenuItem();
        exitMenuItem.setTitle("Exit");
        rootMenu.getMenuItems().add(exitMenuItem);

        Menu bookMenu = new Menu();
        bookMenu.setName("Book menu");

        bookMenuItem.setNextMenu(bookMenu);


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


    }

    public Menu getRootMenu() {
        return rootMenu;
    }

}
