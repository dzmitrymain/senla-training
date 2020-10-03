package com.senla.training.yeutukhovich.bookstore.ui.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.ui.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.ui.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import com.senla.training.yeutukhovich.bookstore.util.constant.MenuNameConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class MenuBuilder {

    private Menu rootMenu;

    @Autowired
    private BookController bookController;
    @Autowired
    private OrderController orderController;
    @Autowired
    private RequestController requestController;

    public Menu getRootMenu() {
        return rootMenu;
    }

    public void buildMenu() {
        rootMenu = new Menu(MenuNameConstant.MAIN_MENU.getMenuName());
        MenuItem previousRootMenuItem = new MenuItem(MenuNameConstant.BACK_TO_MAIN_MENU.getMenuName(), null,
                rootMenu);

        Menu bookMenu = initBookMenu(previousRootMenuItem);
        Menu orderMenu = initOrderMenu(previousRootMenuItem);
        Menu requestMenu = initRequestMenu(previousRootMenuItem);

        MenuItem bookMenuItem = new MenuItem(MenuNameConstant.BOOK_MENU.getMenuName(), null, bookMenu);
        MenuItem orderMenuItem = new MenuItem(MenuNameConstant.ORDER_MENU.getMenuName(), null, orderMenu);
        MenuItem requestMenuItem = new MenuItem(MenuNameConstant.REQUEST_MENU.getMenuName(), null, requestMenu);
        MenuItem exitMenuItem = new MenuItem(MenuNameConstant.EXIT.getMenuName(), null, null);

        Collections.addAll(rootMenu.getMenuItems(), bookMenuItem, orderMenuItem, requestMenuItem, exitMenuItem);
    }

    private Menu initBookMenu(MenuItem previousRootMenuItem) {
        Menu bookMenu = new Menu(MenuNameConstant.BOOK_MENU.getMenuName());

        Menu showAllBooksMenu = initShowAllBooksMenu(bookMenu);
        Menu bookExportMenu = initBookExportMenu(bookMenu);

        MenuItem showAllBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_BOOKS.getMenuName(), null,
                showAllBooksMenu);
        MenuItem showBookDescriptionItem = new MenuItem(MenuNameConstant.SHOW_BOOK_DESCRIPTION.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    UiConsolePrinter.printMessage(bookController.showBookDescription(id));
                }, bookMenu);
        MenuItem replenishBookItem = new MenuItem(MenuNameConstant.REPLENISH_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    UiConsolePrinter.printMessage(bookController.replenishBook(id));
                }, bookMenu);
        MenuItem writeOffBookItem = new MenuItem(MenuNameConstant.WRITE_OFF_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    //TODO: smth
//                    UiConsolePrinter.printMessage(bookController.writeOffBook(id));
                }, bookMenu);
        MenuItem findStaleBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_STALE_BOOKS.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findStaleBooks()),
                bookMenu);
        MenuItem findSoldBooksItem = new MenuItem(MenuNameConstant.SHOW_SOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        UiConsolePrinter.printMessage(bookController.findSoldBooksBetweenDates(firstDate,
                                secondDate));
                    }
                }, bookMenu);
        MenuItem findUnsoldBooksItem = new MenuItem(MenuNameConstant.SHOW_UNSOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        UiConsolePrinter.printMessage(bookController.findUnsoldBooksBetweenDates(firstDate,
                                secondDate));
                    }
                }, bookMenu);
        MenuItem importBooksMenuItem = new MenuItem(MenuNameConstant.IMPORT_BOOKS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();
                    if (fileName != null) {
                        UiConsolePrinter.printMessage(bookController.importBooks(fileName));
                    }
                }, bookMenu);
        MenuItem bookExportMenuItem = new MenuItem(MenuNameConstant.EXPORT_BOOKS.getMenuName(), null,
                bookExportMenu);

        Collections.addAll(bookMenu.getMenuItems(), showAllBooksMenuItem, showBookDescriptionItem,
                replenishBookItem, writeOffBookItem, findStaleBooksMenuItem, findSoldBooksItem,
                findUnsoldBooksItem, importBooksMenuItem, bookExportMenuItem, previousRootMenuItem);
        return bookMenu;
    }

    private Menu initBookExportMenu(Menu previousBookMenu) {
        Menu bookExportMenu = new Menu(MenuNameConstant.EXPORT_BOOKS.getMenuName());

        MenuItem exportBookMenuItem = new MenuItem(MenuNameConstant.EXPORT_BOOK_BY_ID.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long bookId = UserInputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (bookId != null && fileName != null) {
                        UiConsolePrinter.printMessage(bookController.exportBook(bookId, fileName));
                    }
                }, bookExportMenu);
        MenuItem exportAllBooksMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_BOOKS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();
                    if (fileName != null) {
                        UiConsolePrinter.printMessage(bookController.exportAllBooks(fileName));
                    }
                }, bookExportMenu);

        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU.getMenuName(), null,
                previousBookMenu);

        Collections.addAll(bookExportMenu.getMenuItems(), exportBookMenuItem, exportAllBooksMenuItem,
                previousBookMenuItem);
        return bookExportMenu;
    }

    private Menu initShowAllBooksMenu(Menu previousBookMenu) {
        Menu showAllBooksMenu = new Menu(MenuNameConstant.SHOW_ALL_BOOKS.getMenuName());

        MenuItem allBooksSortByTitle = new MenuItem(MenuNameConstant.SORT_BY_TITLE.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByTitle()),
                showAllBooksMenu);
        MenuItem allBooksSortByPrice = new MenuItem(MenuNameConstant.SORT_BY_PRICE.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByPrice()),
                showAllBooksMenu);
        //TODO: rest
        MenuItem allBooksSortByAvailability = new MenuItem(MenuNameConstant.SORT_BY_AVAILABILITY.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByAvailability().toString()),
                showAllBooksMenu);
        MenuItem allBooksSortByEditionYear = new MenuItem(MenuNameConstant.SORT_BY_EDITION_YEAR.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByEditionYear()),
                showAllBooksMenu);
        MenuItem allBooksSortByReplenishmentDate = new MenuItem(MenuNameConstant.SORT_BY_REPLENISHMENT_DATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByReplenishmentDate()),
                showAllBooksMenu);

        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU.getMenuName(), null,
                previousBookMenu);

        Collections.addAll(showAllBooksMenu.getMenuItems(), allBooksSortByTitle, allBooksSortByPrice,
                allBooksSortByAvailability, allBooksSortByEditionYear, allBooksSortByReplenishmentDate,
                previousBookMenuItem);
        return showAllBooksMenu;
    }

    private Menu initOrderMenu(MenuItem previousRootMenuItem) {
        Menu orderMenu = new Menu(MenuNameConstant.ORDER_MENU.getMenuName());
        Menu showAllOrdersMenu = initShowAllOrdersMenu(orderMenu);
        Menu exportOrdersMenu = initExportOrdersMenu(orderMenu);

        MenuItem showAllOrdersMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_ORDERS.getMenuName(), null,
                showAllOrdersMenu);
        MenuItem cancelOrderMenuItem = new MenuItem(MenuNameConstant.CANCEL_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long orderId = UserInputReader.readInputLong();
                    if (orderId != null) {
                        UiConsolePrinter.printMessage(orderController.cancelOrder(orderId));
                    }
                },
                orderMenu);
        MenuItem createOrderMenuItem = new MenuItem(MenuNameConstant.CREATE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_CUSTOMER_DATA.getMessage());
                    String customerData = UserInputReader.readInputString();

                    if (id != null && customerData != null) {
                        UiConsolePrinter.printMessage(orderController.createOrder(id, customerData));
                    }
                },
                orderMenu);
        MenuItem completeOrderMenuItem = new MenuItem(MenuNameConstant.COMPLETE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long orderId = UserInputReader.readInputLong();
                    if (orderId != null) {
                        UiConsolePrinter.printMessage(orderController.completeOrder(orderId));
                    }
                }, orderMenu);
        MenuItem showCompletedOrdersNumberMenuItem =
                new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES.getMenuName(),
                        () -> {
                            System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date firstDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date secondDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            if (firstDate != null && secondDate != null) {
                                System.out.println(MessageConstant.COMPLETED_ORDERS_NUMBER.getMessage() +
                                        orderController
                                                .calculateCompletedOrdersNumberBetweenDates(firstDate, secondDate));
                            }
                        }, orderMenu);
        MenuItem showCompletedOrdersMenuItem =
                new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_BETWEEN_DATES.getMenuName(),
                        () -> {
                            System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date firstDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date secondDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            if (firstDate != null && secondDate != null) {
                                UiConsolePrinter.printMessage(orderController
                                        .findCompletedOrdersBetweenDates(firstDate, secondDate));
                            }
                        }, orderMenu);
        MenuItem showOrderDetailsMenuItem = new MenuItem(MenuNameConstant.SHOW_ORDER_DETAILS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long id = UserInputReader.readInputLong();

                    if (id != null) {
                        UiConsolePrinter.printMessage(orderController.showOrderDetails(id));
                    }
                }, orderMenu);
        MenuItem showProfitBetweenDatesMenuItem = new MenuItem(MenuNameConstant.SHOW_PROFIT_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = UserInputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        System.out.println(MessageConstant.PROFIT.getMessage() + orderController
                                .calculateProfitBetweenDates(firstDate, secondDate));
                    }
                }, orderMenu);
        MenuItem importOrdersMenuItem = new MenuItem(MenuNameConstant.IMPORT_ORDERS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (fileName != null) {
                        UiConsolePrinter.printMessage(orderController.importOrders(fileName));
                    }
                }, orderMenu);
        MenuItem exportOrdersMenuItem = new MenuItem(MenuNameConstant.EXPORT_ORDERS.getMenuName(),
                null, exportOrdersMenu);

        Collections.addAll(orderMenu.getMenuItems(), showAllOrdersMenuItem, createOrderMenuItem, completeOrderMenuItem,
                cancelOrderMenuItem, showOrderDetailsMenuItem, showProfitBetweenDatesMenuItem,
                showCompletedOrdersMenuItem, showCompletedOrdersNumberMenuItem, importOrdersMenuItem,
                exportOrdersMenuItem, previousRootMenuItem);
        return orderMenu;
    }

    private Menu initExportOrdersMenu(Menu previousOrderMenu) {
        Menu exportOrdersMenu = new Menu(MenuNameConstant.EXPORT_ORDERS.getMenuName());

        MenuItem exportOrderMenuItem = new MenuItem(MenuNameConstant.EXPORT_ORDER_BY_ID.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long orderId = UserInputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (orderId != null && fileName != null) {
                        UiConsolePrinter.printMessage(orderController.exportOrder(orderId, fileName));
                    }
                }, exportOrdersMenu);
        MenuItem exportAllOrdersMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_ORDERS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();
                    if (fileName != null) {
                        UiConsolePrinter.printMessage(orderController.exportAllOrders(fileName));
                    }
                }, exportOrdersMenu);
        MenuItem previousOrderMenuItem = new MenuItem(MenuNameConstant.BACK_TO_ORDER_MENU.getMenuName(),
                null, previousOrderMenu);

        Collections.addAll(exportOrdersMenu.getMenuItems(), exportOrderMenuItem, exportAllOrdersMenuItem,
                previousOrderMenuItem);
        return exportOrdersMenu;
    }

    private Menu initShowAllOrdersMenu(Menu previousOrderMenu) {
        Menu showAllOrdersMenu = new Menu(MenuNameConstant.SHOW_ALL_ORDERS.getMenuName());

        MenuItem allOrdersByStateMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(orderController.findSortedAllOrdersByState()),
                showAllOrdersMenu);
        MenuItem allOrdersByPriceMenuItem = new MenuItem(MenuNameConstant.SORT_BY_PRICE.getMenuName(),
                () -> UiConsolePrinter.printMessage(orderController.findSortedAllOrdersByPrice()),
                showAllOrdersMenu);
        MenuItem allOrdersByCompletionMenuItem = new MenuItem(MenuNameConstant.SORT_BY_COMPLETION_DATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(orderController.findSortedAllOrdersByCompletionDate()),
                showAllOrdersMenu);
        MenuItem previousOrderMenuItem = new MenuItem(MenuNameConstant.BACK_TO_ORDER_MENU.getMenuName(),
                null, previousOrderMenu);

        Collections.addAll(showAllOrdersMenu.getMenuItems(), allOrdersByStateMenuItem, allOrdersByPriceMenuItem,
                allOrdersByCompletionMenuItem, previousOrderMenuItem);
        return showAllOrdersMenu;
    }

    private Menu initRequestMenu(MenuItem previousRootMenuItem) {
        Menu requestMenu = new Menu(MenuNameConstant.REQUEST_MENU.getMenuName());
        Menu showAllRequestsMenu = initShowAllRequestsMenu(requestMenu);
        Menu exportRequestMenu = initExportRequestMenu(requestMenu);

        MenuItem showAllRequestsMenuItem = new MenuItem(MenuNameConstant.SHOW_ALL_REQUESTS.getMenuName(),
                null, showAllRequestsMenu);
        MenuItem createRequestMenuItem = new MenuItem(MenuNameConstant.CREATE_REQUEST.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long bookId = UserInputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_REQUESTER_DATA.getMessage());
                    String requesterData = UserInputReader.readInputString();
                    if (bookId != null && requesterData != null) {
                        UiConsolePrinter.printMessage(requestController.createRequest(bookId, requesterData));
                    }
                }, requestMenu);
        MenuItem importRequestsMenuItem = new MenuItem(MenuNameConstant.IMPORT_REQUESTS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (fileName != null) {
                        UiConsolePrinter.printMessage(requestController.importRequests(fileName));
                    }
                }, requestMenu);
        MenuItem exportRequestsMenuItem = new MenuItem(MenuNameConstant.EXPORT_REQUESTS.getMenuName(),
                null, exportRequestMenu);

        Collections.addAll(requestMenu.getMenuItems(), showAllRequestsMenuItem, createRequestMenuItem,
                importRequestsMenuItem, exportRequestsMenuItem,
                previousRootMenuItem);
        return requestMenu;
    }

    private Menu initExportRequestMenu(Menu previousRequestMenu) {
        Menu exportRequestMenu = new Menu(MenuNameConstant.EXPORT_REQUESTS.getMenuName());

        MenuItem exportRequestMenuItem = new MenuItem(MenuNameConstant.EXPORT_REQUEST_BY_ID.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_REQUEST_ID.getMessage());
                    Long requestId = UserInputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (requestId != null && fileName != null) {
                        UiConsolePrinter.printMessage(requestController.exportRequest(requestId, fileName));
                    }
                }, exportRequestMenu);
        MenuItem exportAllRequestsMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_REQUESTS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();

                    if (fileName != null) {
                        UiConsolePrinter.printMessage(requestController.exportAllRequests(fileName));
                    }
                }, exportRequestMenu);

        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU.getMenuName(),
                null, previousRequestMenu);

        Collections.addAll(exportRequestMenu.getMenuItems(), exportRequestMenuItem, exportAllRequestsMenuItem,
                previousRequestMenuItem);
        return exportRequestMenu;
    }

    private Menu initShowAllRequestsMenu(Menu previousRequestMenu) {
        Menu showAllRequestsMenu = new Menu(MenuNameConstant.SHOW_ALL_REQUESTS.getMenuName());

        MenuItem allRequestsSortByBookTitleMenuItem = new MenuItem(MenuNameConstant.SORT_BY_BOOK_TITLE.getMenuName(),
                () -> UiConsolePrinter.printMessage(requestController.findSortedAllRequestsByBookTitle()),
                showAllRequestsMenu);
        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(requestController.findSortedAllRequestsByIsActive()),
                showAllRequestsMenu);
        MenuItem allRequestsSortByRequesterDataMenuItem =
                new MenuItem(MenuNameConstant.SORT_BY_REQUESTER_DATA.getMenuName(),
                        () -> UiConsolePrinter.printMessage(requestController
                                .findSortedAllRequestsByRequesterData()), showAllRequestsMenu);
        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU.getMenuName(),
                null, previousRequestMenu);

        Collections.addAll(showAllRequestsMenu.getMenuItems(), allRequestsSortByBookTitleMenuItem,
                allRequestsSortByIsActiveMenuItem, allRequestsSortByRequesterDataMenuItem, previousRequestMenuItem);
        return showAllRequestsMenu;
    }
}
