package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.controller.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.controller.config.TestWebConfig;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.handler.ControllerExceptionHandler;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.util.constant.EndpointConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.RequestParameterConstant;
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
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfig.class, TestWebConfig.class, RequestController.class, ControllerExceptionHandler.class
})
@WebAppConfiguration
class RequestControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private RequestService requestService;

    private final Long bookId = 1L;
    private final Long requestId = 1L;
    private final String requesterData = "TestRequester";
    private final RequestDto requestDto = new RequestDto(requestId, new BookDto(bookId, "TestBook",
            false, 1000, null, BigDecimal.ONE), true, requesterData);
    private final String expectedRequestDtoJson = "{\"id\":1,\"bookDto\":{\"id\":1,\"title\":\"TestBook\"," +
            "\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"requesterData\":\"TestRequester\",\"active\":true}";
    private final List<RequestDto> requestsDto = List.of(requestDto, requestDto);
    private final String expectedRequestsDtoJson = "[{\"id\":1,\"bookDto\":{\"id\":1,\"title\":\"TestBook\"," +
            "\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"requesterData\":\"TestRequester\",\"active\":true},{\"id\":1,\"bookDto\":{\"id\":1," +
            "\"title\":\"TestBook\",\"editionYear\":1000,\"replenishmentDate\":null,\"price\":1,\"available\":false}," +
            "\"requesterData\":\"TestRequester\",\"active\":true}]";
    private final String exceptionMessage = "Exception message.";
    private final String expectedErrorDtoJson = "{\"status\":\"FORBIDDEN\",\"message\":\"Exception message.\"}";
    private final String importFileName = "TestImport";
    private final String exportFileName = "TestExport";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Mockito.reset(requestService);
    }

    @Test
    void RequestController_createRequest_shouldReturnCreatedRequestDto() throws Exception {
        Mockito.when(requestService.createRequest(Mockito.anyLong(), Mockito.anyString())).thenReturn(requestDto);

        mvc.perform(MockMvcRequestBuilders
                .post(EndpointConstant.REQUESTS_CREATE.getEndpoint())
                .param(RequestParameterConstant.BOOK_ID.getParameterName(), bookId.toString())
                .param(RequestParameterConstant.REQUESTER_DATA.getParameterName(), requesterData))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedRequestDtoJson));
    }

    @Test
    void RequestController_createRequest_shouldReturnErrorDto() throws Exception {
        Mockito.when(requestService.createRequest(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders
                .post(EndpointConstant.REQUESTS_CREATE.getEndpoint())
                .param(RequestParameterConstant.BOOK_ID.getParameterName(), bookId.toString())
                .param(RequestParameterConstant.REQUESTER_DATA.getParameterName(), requesterData))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void RequestController_findSortedAllRequests() throws Exception {
        Mockito.when(requestService.findSortedAllRequests(Mockito.anyString())).thenReturn(requestsDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.REQUESTS_BY_BOOK_TITLE.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedRequestsDtoJson));
    }


    @Test
    void RequestController_importRequests_shouldReturnImportedRequestsDto() throws Exception {
        Mockito.when(requestService.importRequests(Mockito.anyString())).thenReturn(requestsDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.REQUESTS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedRequestsDtoJson));
    }

    @Test
    void RequestController_importRequests_shouldReturnErrorDto() throws Exception {
        Mockito.when(requestService.importRequests(Mockito.anyString())).thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.REQUESTS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void RequestController_exportAllRequests() throws Exception {
        Mockito.when(requestService.exportAllRequests(Mockito.anyString())).thenReturn(requestsDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.REQUESTS_EXPORT_ALL.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedRequestsDtoJson));
    }

    @Test
    void RequestController_exportRequest_shouldReturnExportedRequestDto() throws Exception {
        Mockito.when(requestService.exportRequest(Mockito.anyLong(), Mockito.anyString())).thenReturn(requestDto);

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.REQUESTS_EXPORT.getEndpoint(), requestId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedRequestDtoJson));
    }

    @Test
    void RequestController_exportRequest_shouldReturnErrorDto() throws Exception {
        Mockito.when(requestService.exportRequest(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new BusinessException(exceptionMessage, HttpStatus.FORBIDDEN));

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.REQUESTS_EXPORT.getEndpoint(), requestId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }
}
