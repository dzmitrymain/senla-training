package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.controller.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.controller.config.TestWebConfig;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDetailsDto;
import com.senla.training.yeutukhovich.bookstore.dto.OrderDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.handler.ControllerExceptionHandler;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.util.constant.EndpointConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.RequestParameterConstant;
import com.senla.training.yeutukhovich.bookstore.util.converter.DateConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfig.class, TestWebConfig.class, OrderController.class, ControllerExceptionHandler.class
})
@WebAppConfiguration
class OrderControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private OrderService orderService;

    private final Long bookId = 1L;
    private final Long orderId = 1L;
    private final String customerData = "TestCustomer";
    private final OrderDto orderDto = new OrderDto(orderId, "CREATED",
            new BookDto(bookId, "TestBook", false, 1000,
                    null, BigDecimal.ONE), BigDecimal.ONE,
            DateConverter.parseDate("2000-01-01", DateConverter.DAY_DATE_FORMAT),
            null, customerData);
    private final String expectedOrderDtoJson = "{\"id\":1,\"state\":\"CREATED\",\"bookDto\":{\"id\":1," +
            "\"title\":\"TestBook\",\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"price\":1,\"creationDate\":\"Sat Jan 01 00:00:00 MSK 2000\",\"completionDate\":null," +
            "\"customerData\":\"TestCustomer\"}";
    private final List<OrderDto> ordersDto = List.of(orderDto, orderDto);
    private final String expectedOrdersDtoJson = "[{\"id\":1,\"state\":\"CREATED\",\"bookDto\":{\"id\":1," +
            "\"title\":\"TestBook\",\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"price\":1,\"creationDate\":\"Sat Jan 01 00:00:00 MSK 2000\",\"completionDate\":null," +
            "\"customerData\":\"TestCustomer\"},{\"id\":1,\"state\":\"CREATED\",\"bookDto\":{\"id\":1," +
            "\"title\":\"TestBook\",\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"price\":1,\"creationDate\":\"Sat Jan 01 00:00:00 MSK 2000\",\"completionDate\":null," +
            "\"customerData\":\"TestCustomer\"}]";
    private final String exceptionMessage = "Exception message.";
    private final String expectedErrorDtoJson = "{\"status\":\"FORBIDDEN\",\"message\":\"Exception message.\"}";
    private final String startDate = "2000-01-01";
    private final String endDate = "2100-01-01";
    private final String importFileName = "TestImport";
    private final String exportFileName = "TestExport";

    OrderControllerTest() throws ParseException {
    }

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Mockito.reset(orderService);
    }

    @Test
    void OrderController_createOrder_shouldReturnCreatedOrderDto() throws Exception {
        Mockito.when(orderService.createOrder(Mockito.anyLong(), Mockito.anyString())).thenReturn(orderDto);

        mvc.perform(MockMvcRequestBuilders
                .post(EndpointConstant.ORDERS_CREATE.getEndpoint())
                .param(RequestParameterConstant.BOOK_ID.getParameterName(), bookId.toString())
                .param(RequestParameterConstant.CUSTOMER_DATA.getParameterName(), customerData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrderDtoJson));
    }

    @Test
    void OrderController_createOrder_shouldReturnErrorDto() throws Exception {
        Mockito.when(orderService.createOrder(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders
                .post(EndpointConstant.ORDERS_CREATE.getEndpoint())
                .param(RequestParameterConstant.BOOK_ID.getParameterName(), bookId.toString())
                .param(RequestParameterConstant.CUSTOMER_DATA.getParameterName(), customerData))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void OrderController_updateState_shouldReturnUpdatedOrderDto() throws Exception {
        Mockito.when(orderService.updateState(Mockito.anyLong(), Mockito.any(OrderDto.class))).thenReturn(orderDto);

        mvc.perform(MockMvcRequestBuilders
                .put(String.format(EndpointConstant.ORDERS_UPDATE_STATE.getEndpoint(), orderId))
                .content(expectedOrderDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrderDtoJson));
    }

    @Test
    void OrderController_updateState_shouldReturnErrorDto() throws Exception {
        Mockito.when(orderService.updateState(Mockito.anyLong(), Mockito.any(OrderDto.class))).thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders
                .put(String.format(EndpointConstant.ORDERS_UPDATE_STATE.getEndpoint(), orderId))
                .content(expectedOrderDtoJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void OrderController_findSortedAllOrders() throws Exception {
        Mockito.when(orderService.findSortedAllOrders(Mockito.anyString())).thenReturn(ordersDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.ORDERS_BY_COMPLETION_DATE.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrdersDtoJson));
    }

    @Test
    void OrderController_findCompletedOrdersBetweenDates() throws Exception {
        Mockito.when(orderService.findCompletedOrdersBetweenDates(Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(ordersDto);

        mvc.perform(MockMvcRequestBuilders.get(
                String.format(EndpointConstant.ORDERS_COMPLETED_BETWEEN_DATES.getEndpoint(), startDate, endDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrdersDtoJson));
    }

    @Test
    void OrderController_calculateProfitBetweenDates() throws Exception {
        Mockito.when(orderService.calculateProfitBetweenDates(Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(BigDecimal.TEN);

        String expectedMapJson = "{\"profit\":10}";

        mvc.perform(MockMvcRequestBuilders.get(
                String.format(EndpointConstant.ORDERS_PROFIT_BETWEEN_DATES.getEndpoint(), startDate, endDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedMapJson));
    }

    @Test
    void OrderController_calculateCompletedOrdersNumberBetweenDates() throws Exception {
        Mockito.when(orderService.calculateCompletedOrdersNumberBetweenDates(Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(10L);

        String expectedMapJson = "{\"ordersNumber\":10}";

        mvc.perform(MockMvcRequestBuilders.get(
                String.format(EndpointConstant.ORDERS_NUMBER_BETWEEN_DATES.getEndpoint(), startDate, endDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedMapJson));
    }

    @Test
    void OrderController_showOrderDetails_shouldReturnOrderDetailsDto() throws Exception {
        OrderDetailsDto orderDetailsDto = new OrderDetailsDto("TestCustomer", "TestBook",
                "CREATED", BigDecimal.ONE,
                DateConverter.parseDate("2000-01-01", DateConverter.DAY_DATE_FORMAT), null);
        String expectedOrderDetailsDtoJson = "{\"customerData\":\"TestCustomer\",\"bookTitle\":\"TestBook\",\"state\":\"" +
                "CREATED\",\"price\":1,\"creationDate\":\"Sat Jan 01 00:00:00 MSK 2000\",\"completionDate\":null}";
        Mockito.when(orderService.showOrderDetails(orderId)).thenReturn(orderDetailsDto);

        mvc.perform(MockMvcRequestBuilders.get(String.format(EndpointConstant.ORDERS_DETAILS.getEndpoint(), orderId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrderDetailsDtoJson));
    }

    @Test
    void OrderController_showOrderDetails_shouldReturnErrorDto() throws Exception {
        Mockito.when(orderService.showOrderDetails(orderId)).thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders.get(String.format(EndpointConstant.ORDERS_DETAILS.getEndpoint(), orderId)))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void OrderController_importOrders_shouldReturnImportedOrdersDto() throws Exception {
        Mockito.when(orderService.importOrders(Mockito.anyString())).thenReturn(ordersDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.ORDERS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrdersDtoJson));
    }

    @Test
    void OrderController_importOrders_shouldReturnErrorDto() throws Exception {
        Mockito.when(orderService.importOrders(Mockito.anyString())).thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.ORDERS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void OrderController_exportAllOrders() throws Exception {
        Mockito.when(orderService.exportAllOrders(Mockito.anyString())).thenReturn(ordersDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.ORDERS_EXPORT_ALL.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrdersDtoJson));
    }

    @Test
    void OrderController_exportOrder_shouldReturnExportedOrderDto() throws Exception {
        Mockito.when(orderService.exportOrder(Mockito.anyLong(), Mockito.anyString())).thenReturn(orderDto);

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.ORDERS_EXPORT.getEndpoint(), orderId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedOrderDtoJson));
    }

    @Test
    void OrderController_exportOrder_shouldReturnErrorDto() throws Exception {
        Mockito.when(orderService.exportOrder(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.ORDERS_EXPORT.getEndpoint(), orderId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }
}
