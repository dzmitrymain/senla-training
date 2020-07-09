package com.senla.training.yeutukhovich.bookstore.ui.menu.builder;

import com.senla.training.yeutukhovich.bookstore.controller.BookController;
import com.senla.training.yeutukhovich.bookstore.controller.OrderController;
import com.senla.training.yeutukhovich.bookstore.controller.RequestController;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.service.dto.CreationOrderResult;
import com.senla.training.yeutukhovich.bookstore.service.dto.OrderDetails;
import com.senla.training.yeutukhovich.bookstore.ui.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.ui.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.ui.util.printer.UiConsolePrinter;
import com.senla.training.yeutukhovich.bookstore.util.constant.MenuNameConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.reader.InputReader;

import java.util.Collections;
import java.util.Date;

@Singleton
public class MenuBuilder {

    private Menu rootMenu;

    @Autowired
    private BookController bookController;
    @Autowired
    private OrderController orderController;
    @Autowired
    private RequestController requestController;

    private MenuBuilder() {

    }

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
                    Long id = InputReader.readInputLong();

                    BookDescription bookDescription = bookController.showBookDescription(id);
                    if (bookDescription != null) {
                        System.out.println(bookDescription);
                    } else {
                        System.out.println(MessageConstant.BOOK_DESCRIPTION_WAS_NOT_FOUND.getMessage());
                    }
                }, bookMenu);
        MenuItem replenishBookItem = new MenuItem(MenuNameConstant.REPLENISH_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = InputReader.readInputLong();
                    UiConsolePrinter.printMessage(bookController.replenishBook(id));
                }, bookMenu);
        MenuItem writeOffBookItem = new MenuItem(MenuNameConstant.WRITE_OFF_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = InputReader.readInputLong();
                    UiConsolePrinter.printMessage(bookController.writeOffBook(id));
                }, bookMenu);
        MenuItem findStaleBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_STALE_BOOKS.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findStaleBooks())
                , bookMenu);
        MenuItem findSoldBooksItem = new MenuItem(MenuNameConstant.SHOW_SOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        UiConsolePrinter.printMessage(bookController.findSoldBooksBetweenDates(firstDate,
                                secondDate));
                    }
                }, bookMenu);
        MenuItem findUnsoldBooksItem = new MenuItem(MenuNameConstant.SHOW_UNSOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        UiConsolePrinter.printMessage(bookController.findUnsoldBooksBetweenDates(firstDate,
                                secondDate));
                    }
                }, bookMenu);
        MenuItem importBooksMenuItem = new MenuItem(MenuNameConstant.IMPORT_BOOKS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    int importedBooksNumber = 0;
                    if (fileName != null) {
                        importedBooksNumber = bookController.importBooks(fileName);
                    }

                    System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedBooksNumber);
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
                    Long bookId = InputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    if (bookId != null && fileName != null) {
                        if (bookController.exportBook(bookId, fileName)) {
                            System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
                        } else {
                            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
                        }
                    }
                }, bookExportMenu);
        MenuItem exportAllBooksMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_BOOKS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();
                    int exportedBooksNumber = 0;
                    if (fileName != null) {
                        exportedBooksNumber = bookController.exportAllBooks(fileName);
                    }
                    System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedBooksNumber);
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
        MenuItem allBooksSortByAvailability = new MenuItem(MenuNameConstant.SORT_BY_AVAILABILITY.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByAvailability()),
                showAllBooksMenu);
        MenuItem allBooksSortByEditionDate = new MenuItem(MenuNameConstant.SORT_BY_EDITION_DATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByEditionDate()),
                showAllBooksMenu);
        MenuItem allBooksSortByReplenishmentDate = new MenuItem(MenuNameConstant.SORT_BY_REPLENISHMENT_DATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(bookController.findSortedAllBooksByReplenishmentDate()),
                showAllBooksMenu);

        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU.getMenuName(), null,
                previousBookMenu);

        Collections.addAll(showAllBooksMenu.getMenuItems(), allBooksSortByTitle, allBooksSortByPrice,
                allBooksSortByAvailability, allBooksSortByEditionDate, allBooksSortByReplenishmentDate,
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
                    Long orderId = InputReader.readInputLong();
                    if (orderId != null) {
                        if (orderController.cancelOrder(orderId)) {
                            System.out.println(MessageConstant.ORDER_HAS_BEEN_CANCELED.getMessage());
                        } else {
                            System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_CANCELED.getMessage());
                        }
                    }
                },
                orderMenu);
        MenuItem createOrderMenuItem = new MenuItem(MenuNameConstant.CREATE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = InputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_CUSTOMER_DATA.getMessage());
                    String customerData = InputReader.readInputString();

                    if (id != null && customerData != null) {
                        CreationOrderResult creationOrderResult = orderController
                                .createOrder(id, customerData);
                        if (creationOrderResult.getOrderId() != null) {
                            System.out.println(MessageConstant.ORDER_HAS_BEEN_CREATED.getMessage());
                            if (creationOrderResult.getRequestId() != null) {
                                System.out.println(MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage());
                            }
                        } else {
                            System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_CREATED.getMessage());
                        }
                    }
                },
                orderMenu);
        MenuItem completeOrderMenuItem = new MenuItem(MenuNameConstant.COMPLETE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long orderId = InputReader.readInputLong();

                    if (orderId != null) {
                        if (orderController.completeOrder(orderId)) {
                            System.out.println(MessageConstant.ORDER_HAS_BEEN_COMPLETED.getMessage());
                        } else {
                            System.out.println(MessageConstant.ORDER_HAS_NOT_BEEN_COMPLETED.getMessage());
                        }
                    }
                }, orderMenu);
        MenuItem showCompletedOrdersNumberMenuItem =
                new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES.getMenuName(),
                        () -> {
                            System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

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
                            Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                            if (firstDate != null && secondDate != null) {
                                UiConsolePrinter.printMessage(orderController
                                        .findCompletedOrdersBetweenDates(firstDate, secondDate));
                            }
                        }, orderMenu);
        MenuItem showOrderDetailsMenuItem = new MenuItem(MenuNameConstant.SHOW_ORDER_DETAILS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long id = InputReader.readInputLong();

                    if (id != null) {
                        OrderDetails orderDetails = orderController.showOrderDetails(id);
                        if (orderDetails != null) {
                            System.out.println(orderDetails);
                        } else {
                            System.out.println(MessageConstant.ORDER_DETAILS_WAS_NOT_FOUND.getMessage());
                        }
                    }
                }, orderMenu);
        MenuItem showProfitBetweenDatesMenuItem = new MenuItem(MenuNameConstant.SHOW_PROFIT_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date firstDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    Date secondDate = InputReader.readInputDate(DateConverter.DAY_DATE_FORMAT);

                    if (firstDate != null && secondDate != null) {
                        System.out.println(MessageConstant.PROFIT.getMessage() + orderController
                                .calculateProfitBetweenDates(firstDate, secondDate));
                    }
                }, orderMenu);
        MenuItem importOrdersMenuItem = new MenuItem(MenuNameConstant.IMPORT_ORDERS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    int importedOrdersNumber = 0;
                    if (fileName != null) {
                        importedOrdersNumber = orderController.importOrders(fileName);
                    }

                    System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedOrdersNumber);
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
                    Long orderId = InputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    if (orderId != null && fileName != null) {
                        if (orderController.exportOrder(orderId, fileName)) {
                            System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
                        } else {
                            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
                        }
                    }
                }, exportOrdersMenu);
        MenuItem exportAllOrdersMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_ORDERS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();
                    int exportedOrdersNumber = 0;
                    if (fileName != null) {
                        exportedOrdersNumber = orderController.exportAllOrders(fileName);
                    }
                    System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedOrdersNumber);
                }
                , exportOrdersMenu);

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
                    Long bookId = InputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_REQUESTER_DATA.getMessage());
                    String requesterData = InputReader.readInputString();

                    if (bookId != null && requesterData != null) {

                        Long requestId = requestController.createRequest(bookId, requesterData);
                        if (requestId != null) {
                            System.out.println(MessageConstant.REQUEST_HAS_BEEN_CREATED.getMessage());
                        } else {
                            System.out.println(MessageConstant.REQUEST_HAS_NOT_BEEN_CREATED.getMessage());
                        }
                    }
                }, requestMenu);
        MenuItem importRequestsMenuItem = new MenuItem(MenuNameConstant.IMPORT_REQUESTS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    int importedRequestsNumber = 0;
                    if (fileName != null) {
                        importedRequestsNumber = requestController.importRequests(fileName);
                    }

                    System.out.println(MessageConstant.IMPORTED_ENTITIES.getMessage() + importedRequestsNumber);
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
                    Long requestId = InputReader.readInputLong();

                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();

                    if (requestId != null && fileName != null) {
                        if (requestController.exportRequest(requestId, fileName)) {
                            System.out.println(MessageConstant.ENTITY_EXPORTED.getMessage());
                        } else {
                            System.out.println(MessageConstant.ENTITY_NOT_EXPORTED.getMessage());
                        }
                    }
                }, exportRequestMenu);
        MenuItem exportAllRequestsMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_REQUESTS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = InputReader.readInputString();
                    int exportedRequestsNumber = 0;
                    if (fileName != null) {
                        exportedRequestsNumber = requestController.exportAllRequests(fileName);
                    }
                    System.out.println(MessageConstant.EXPORTED_ENTITIES.getMessage() + exportedRequestsNumber);
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
                () -> UiConsolePrinter.printMessage(requestController.findSortedAllRequestsByBookTitle())
                , showAllRequestsMenu);
        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE.getMenuName(),
                () -> UiConsolePrinter.printMessage(requestController.findSortedAllRequestsByIsActive())
                , showAllRequestsMenu);
        MenuItem allRequestsSortByRequesterDataMenuItem =
                new MenuItem(MenuNameConstant.SORT_BY_REQUESTER_DATA.getMenuName(),
                        () -> UiConsolePrinter.printMessage(requestController
                                .findSortedAllRequestsByRequesterData())
                        , showAllRequestsMenu);
        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU.getMenuName(),
                null, previousRequestMenu);

        Collections.addAll(showAllRequestsMenu.getMenuItems(), allRequestsSortByBookTitleMenuItem,
                allRequestsSortByIsActiveMenuItem, allRequestsSortByRequesterDataMenuItem, previousRequestMenuItem);
        return showAllRequestsMenu;
    }
}
