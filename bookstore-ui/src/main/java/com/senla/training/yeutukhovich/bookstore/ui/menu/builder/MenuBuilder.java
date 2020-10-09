package com.senla.training.yeutukhovich.bookstore.ui.menu.builder;

import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.ui.menu.Menu;
import com.senla.training.yeutukhovich.bookstore.ui.menu.MenuItem;
import com.senla.training.yeutukhovich.bookstore.ui.util.constant.EndpointConstant;
import com.senla.training.yeutukhovich.bookstore.ui.util.constant.RequestParameterConstant;
import com.senla.training.yeutukhovich.bookstore.ui.util.reader.UserInputReader;
import com.senla.training.yeutukhovich.bookstore.ui.util.webclient.RestTemplateHelper;
import com.senla.training.yeutukhovich.bookstore.util.constant.MenuNameConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Component
public class MenuBuilder {

    @Value("${webcontext.url}")
    private String webContextUrl;

    private Menu rootMenu;

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
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.BOOKS_DESCRIPTION.getEndpoint(), id), HttpMethod.GET,
                            null, BookDescriptionDto.class);
                }, bookMenu);
        MenuItem replenishBookItem = new MenuItem(MenuNameConstant.REPLENISH_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.BOOKS_REPLENISH.getEndpoint(), id), HttpMethod.POST,
                            null, BookDto.class);
                }, bookMenu);
        MenuItem writeOffBookItem = new MenuItem(MenuNameConstant.WRITE_OFF_BOOK.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.BOOKS_WRITE_OFF.getEndpoint(), id), HttpMethod.POST,
                            null, BookDto.class);
                }, bookMenu);
        MenuItem findStaleBooksMenuItem = new MenuItem(MenuNameConstant.SHOW_STALE_BOOKS.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_STALE.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
                bookMenu);
        MenuItem findSoldBooksItem = new MenuItem(MenuNameConstant.SHOW_SOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String firstDate = UserInputReader.readInputString();
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String secondDate = UserInputReader.readInputString();

                    RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                    String.format(EndpointConstant.BOOKS_SOLD_BETWEEN_DATES.getEndpoint(), firstDate, secondDate),
                            HttpMethod.GET, null, BookDto[].class);
                }, bookMenu);
        MenuItem findUnsoldBooksItem = new MenuItem(MenuNameConstant.SHOW_UNSOLD_BOOKS_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String firstDate = UserInputReader.readInputString();
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String secondDate = UserInputReader.readInputString();

                    RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                            String.format(EndpointConstant.BOOKS_UNSOLD_BETWEEN_DATES.getEndpoint(), firstDate,
                                    secondDate), HttpMethod.GET, null, BookDto[].class);
                }, bookMenu);
        MenuItem importBooksMenuItem = new MenuItem(MenuNameConstant.IMPORT_BOOKS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
                    String fileName = UserInputReader.readInputString();
                    if (fileName == null) {
                        return;
                    }
                    HttpEntity<MultiValueMap<String, String>> request = RestTemplateHelper.initUrlencodedRequest(
                            List.of(fileName), RequestParameterConstant.FILE_NAME.getParameterName());
                    RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                            EndpointConstant.BOOKS_IMPORT.getEndpoint(), HttpMethod.POST, request, BookDto[].class);
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
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.BOOKS_EXPORT.getEndpoint(), bookId), HttpMethod.POST,
                            createRequestWithInputtedFileName(), BookDto.class);
                }, bookExportMenu);
        MenuItem exportAllBooksMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_BOOKS.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                EndpointConstant.BOOKS_EXPORT_ALL.getEndpoint(), HttpMethod.POST,
                        createRequestWithInputtedFileName(), BookDto[].class), bookExportMenu);

        MenuItem previousBookMenuItem = new MenuItem(MenuNameConstant.BACK_TO_BOOK_MENU.getMenuName(), null,
                previousBookMenu);

        Collections.addAll(bookExportMenu.getMenuItems(), exportBookMenuItem, exportAllBooksMenuItem,
                previousBookMenuItem);
        return bookExportMenu;
    }

    private Menu initShowAllBooksMenu(Menu previousBookMenu) {
        Menu showAllBooksMenu = new Menu(MenuNameConstant.SHOW_ALL_BOOKS.getMenuName());

        MenuItem allBooksSortByTitle = new MenuItem(MenuNameConstant.SORT_BY_TITLE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_BY_TITLE.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
                showAllBooksMenu);
        MenuItem allBooksSortByPrice = new MenuItem(MenuNameConstant.SORT_BY_PRICE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_BY_PRICE.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
                showAllBooksMenu);
        MenuItem allBooksSortByAvailability = new MenuItem(MenuNameConstant.SORT_BY_AVAILABILITY.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_BY_AVAILABILITY.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
                showAllBooksMenu);
        MenuItem allBooksSortByEditionYear = new MenuItem(MenuNameConstant.SORT_BY_EDITION_YEAR.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_BY_EDITION_YEAR.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
                showAllBooksMenu);
        MenuItem allBooksSortByReplenishmentDate = new MenuItem(MenuNameConstant.SORT_BY_REPLENISHMENT_DATE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.BOOKS_BY_REPLENISHMENT.getEndpoint(), HttpMethod.GET, null, BookDto[].class),
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
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_CANCEL.getEndpoint(), orderId), HttpMethod.POST,
                            null, OrderDto.class);
                },
                orderMenu);
        MenuItem createOrderMenuItem = new MenuItem(MenuNameConstant.CREATE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_BOOK_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    System.out.println(MessageConstant.ENTER_CUSTOMER_DATA.getMessage());
                    String customerData = UserInputReader.readInputString();
                    if (customerData == null) {
                        return;
                    }
                    HttpEntity<MultiValueMap<String, String>> request = RestTemplateHelper.initUrlencodedRequest(
                            List.of(customerData, Objects.requireNonNull(id).toString()),
                            RequestParameterConstant.CUSTOMER_DATA.getParameterName(),
                            RequestParameterConstant.BOOK_ID.getParameterName());
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                            EndpointConstant.ORDERS_CREATE.getEndpoint(), HttpMethod.POST, request, OrderDto.class);
                },
                orderMenu);
        MenuItem completeOrderMenuItem = new MenuItem(MenuNameConstant.COMPLETE_ORDER.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long orderId = UserInputReader.readInputLong();
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_COMPLETE.getEndpoint(), orderId),
                            HttpMethod.POST, null, OrderDto.class);
                }, orderMenu);
        MenuItem showCompletedOrdersNumberMenuItem =
                new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_NUMBER_BETWEEN_DATES.getMenuName(),
                        () -> {
                            System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            String firstDate = UserInputReader.readInputString();
                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            String secondDate = UserInputReader.readInputString();

                            RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_NUMBER_BETWEEN_DATES.getEndpoint(),
                                            firstDate, secondDate), HttpMethod.GET, null, Map.class);
                        }, orderMenu);
        MenuItem showCompletedOrdersMenuItem =
                new MenuItem(MenuNameConstant.SHOW_COMPLETED_ORDERS_BETWEEN_DATES.getMenuName(),
                        () -> {
                            System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            String firstDate = UserInputReader.readInputString();
                            System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                            String secondDate = UserInputReader.readInputString();

                            RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_COMPLETED_BETWEEN_DATES.getEndpoint(),
                                            firstDate, secondDate), HttpMethod.GET, null, OrderDto[].class);
                        }, orderMenu);
        MenuItem showOrderDetailsMenuItem = new MenuItem(MenuNameConstant.SHOW_ORDER_DETAILS.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.ENTER_ORDER_ID.getMessage());
                    Long id = UserInputReader.readInputLong();
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_DETAILS.getEndpoint(), id), HttpMethod.GET,
                            null, OrderDetailsDto.class);
                }, orderMenu);
        MenuItem showProfitBetweenDatesMenuItem = new MenuItem(MenuNameConstant.SHOW_PROFIT_BETWEEN_DATES.getMenuName(),
                () -> {
                    System.out.println(MessageConstant.EARLIEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String firstDate = UserInputReader.readInputString();
                    System.out.println(MessageConstant.LATEST_DATE_BOUND_YYYY_MM_DD.getMessage());
                    String secondDate = UserInputReader.readInputString();

                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                            String.format(EndpointConstant.ORDERS_PROFIT_BETWEEN_DATES.getEndpoint(),
                                    firstDate, secondDate), HttpMethod.GET, null, Map.class);
                }, orderMenu);
        MenuItem importOrdersMenuItem = new MenuItem(MenuNameConstant.IMPORT_ORDERS.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                EndpointConstant.ORDERS_IMPORT.getEndpoint(), HttpMethod.POST, createRequestWithInputtedFileName(),
                        OrderDto[].class), orderMenu);
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
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.ORDERS_EXPORT.getEndpoint(), orderId),
                            HttpMethod.POST, createRequestWithInputtedFileName(), OrderDto.class);
                }, exportOrdersMenu);
        MenuItem exportAllOrdersMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_ORDERS.getMenuName(),
                () ->
                        RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                        EndpointConstant.ORDERS_EXPORT_ALL.getEndpoint(), HttpMethod.POST,
                                createRequestWithInputtedFileName(), OrderDto[].class), exportOrdersMenu);
        MenuItem previousOrderMenuItem = new MenuItem(MenuNameConstant.BACK_TO_ORDER_MENU.getMenuName(),
                null, previousOrderMenu);

        Collections.addAll(exportOrdersMenu.getMenuItems(), exportOrderMenuItem, exportAllOrdersMenuItem,
                previousOrderMenuItem);
        return exportOrdersMenu;
    }

    private Menu initShowAllOrdersMenu(Menu previousOrderMenu) {
        Menu showAllOrdersMenu = new Menu(MenuNameConstant.SHOW_ALL_ORDERS.getMenuName());

        MenuItem allOrdersByStateMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.ORDERS_BY_STATE.getEndpoint(), HttpMethod.GET, null, OrderDto[].class),
                showAllOrdersMenu);
        MenuItem allOrdersByPriceMenuItem = new MenuItem(MenuNameConstant.SORT_BY_PRICE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.ORDERS_BY_PRICE.getEndpoint(), HttpMethod.GET, null, OrderDto[].class),
                showAllOrdersMenu);
        MenuItem allOrdersByCompletionMenuItem = new MenuItem(MenuNameConstant.SORT_BY_COMPLETION_DATE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.ORDERS_BY_COMPLETION_DATE.getEndpoint(), HttpMethod.GET, null, OrderDto[].class),
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
                    if (requesterData == null) {
                        return;
                    }
                    HttpEntity<MultiValueMap<String, String>> request = RestTemplateHelper.initUrlencodedRequest(
                            List.of(requesterData, Objects.requireNonNull(bookId).toString()),
                            RequestParameterConstant.REQUESTER_DATA.getParameterName(),
                            RequestParameterConstant.BOOK_ID.getParameterName());
                    RestTemplateHelper.exchangeForEntity(webContextUrl + EndpointConstant.REQUESTS_CREATE.getEndpoint(),
                            HttpMethod.POST, request, RequestDto.class);
                }, requestMenu);
        MenuItem importRequestsMenuItem = new MenuItem(MenuNameConstant.IMPORT_REQUESTS.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                EndpointConstant.REQUESTS_IMPORT.getEndpoint(), HttpMethod.POST, createRequestWithInputtedFileName(),
                        RequestDto[].class), requestMenu);
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
                    RestTemplateHelper.exchangeForEntity(webContextUrl +
                                    String.format(EndpointConstant.REQUESTS_EXPORT.getEndpoint(), requestId),
                            HttpMethod.POST, createRequestWithInputtedFileName(), RequestDto.class);
                }, exportRequestMenu);
        MenuItem exportAllRequestsMenuItem = new MenuItem(MenuNameConstant.EXPORT_ALL_REQUESTS.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                EndpointConstant.REQUESTS_EXPORT_ALL.getEndpoint(), HttpMethod.POST,
                        createRequestWithInputtedFileName(), RequestDto[].class), exportRequestMenu);

        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU.getMenuName(),
                null, previousRequestMenu);

        Collections.addAll(exportRequestMenu.getMenuItems(), exportRequestMenuItem, exportAllRequestsMenuItem,
                previousRequestMenuItem);
        return exportRequestMenu;
    }

    private Menu initShowAllRequestsMenu(Menu previousRequestMenu) {
        Menu showAllRequestsMenu = new Menu(MenuNameConstant.SHOW_ALL_REQUESTS.getMenuName());

        MenuItem allRequestsSortByBookTitleMenuItem = new MenuItem(MenuNameConstant.SORT_BY_BOOK_TITLE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.REQUESTS_BY_BOOK_TITLE.getEndpoint(), HttpMethod.GET, null, RequestDto[].class),
                showAllRequestsMenu);
        MenuItem allRequestsSortByIsActiveMenuItem = new MenuItem(MenuNameConstant.SORT_BY_STATE.getMenuName(),
                () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                        EndpointConstant.REQUESTS_BY_STATE.getEndpoint(), HttpMethod.GET, null, RequestDto[].class),
                showAllRequestsMenu);
        MenuItem allRequestsSortByRequesterDataMenuItem =
                new MenuItem(MenuNameConstant.SORT_BY_REQUESTER_DATA.getMenuName(),
                        () -> RestTemplateHelper.exchangeForEntityArray(webContextUrl +
                                        EndpointConstant.REQUESTS_BY_REQUESTER_DATA.getEndpoint(), HttpMethod.GET, null,
                                RequestDto[].class),
                        showAllRequestsMenu);
        MenuItem previousRequestMenuItem = new MenuItem(MenuNameConstant.BACK_TO_REQUEST_MENU.getMenuName(),
                null, previousRequestMenu);

        Collections.addAll(showAllRequestsMenu.getMenuItems(), allRequestsSortByBookTitleMenuItem,
                allRequestsSortByIsActiveMenuItem, allRequestsSortByRequesterDataMenuItem, previousRequestMenuItem);
        return showAllRequestsMenu;
    }

    private HttpEntity<MultiValueMap<String, String>> createRequestWithInputtedFileName() {
        System.out.println(MessageConstant.ENTER_FILE_NAME.getMessage());
        String fileName = UserInputReader.readInputString();
        if (fileName == null) {
            return null;
        }
        return RestTemplateHelper.initUrlencodedRequest(List.of(fileName),
                RequestParameterConstant.FILE_NAME.getParameterName());
    }
}
