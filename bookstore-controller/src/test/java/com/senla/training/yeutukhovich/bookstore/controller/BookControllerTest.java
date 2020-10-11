package com.senla.training.yeutukhovich.bookstore.controller;

import com.senla.training.yeutukhovich.bookstore.controller.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.controller.config.TestWebConfig;
import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.dto.BookDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.handler.ControllerExceptionHandler;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.util.constant.EndpointConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.RequestParameterConstant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Date;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestConfig.class, TestWebConfig.class, BookController.class, ControllerExceptionHandler.class
})
@WebAppConfiguration
class BookControllerTest {

    private MockMvc mvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private BookService bookService;

    private final Long bookId = 1L;
    private final BookDto bookDto = new BookDto(bookId, "FirstTestBook", false,
            1000, null, BigDecimal.ONE);
    private final String expectedBookDtoJson = "{\"id\":1,\"title\":\"FirstTestBook\",\"editionYear\":1000," +
            "\"replenishmentDate\":null,\"price\":1,\"available\":false}";
    private final List<BookDto> booksDto = List.of(bookDto, new BookDto(bookId, "SecondTestBook",
            false, 2000, null, BigDecimal.TEN));
    private final String expectedBooksDtoJson = "[{\"id\":1,\"title\":\"FirstTestBook\",\"editionYear\":1000," +
            "\"replenishmentDate\":null,\"price\":1,\"available\":false},{\"id\":1,\"title\":\"SecondTestBook\"," +
            "\"editionYear\":2000,\"replenishmentDate\":null,\"price\":10,\"available\":false}]";
    private final String exceptionMessage = "Exception message.";
    private final String expectedErrorDtoJson = "{\"status\":\"OK\",\"message\":\"Exception message.\"}";
    private final BookDescriptionDto bookDescriptionDto = new BookDescriptionDto(
            "FirstTestBook", 1000, null);

    private final String startDate = "2000-01-01";
    private final String endDate = "2100-01-01";
    private final String importFileName = "TestImport";
    private final String exportFileName = "TestExport";

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
        Mockito.reset(bookService);
    }

    @Test
    void BookController_replenishBook_shouldReturnReplenishedBookDto() throws Exception {
        Mockito.when(bookService.replenishBook(bookId)).thenReturn(bookDto);

        mvc.perform(MockMvcRequestBuilders
                .post(String.format(EndpointConstant.BOOKS_REPLENISH.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBookDtoJson));
    }

    @Test
    void BookController_replenishBook_shouldReturnErrorDto() throws Exception {
        Mockito.when(bookService.replenishBook(bookId)).thenThrow(new BusinessException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders
                .post(String.format(EndpointConstant.BOOKS_REPLENISH.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void BookController_writeOffBook_shouldReturnWrittenOffBookDto() throws Exception {
        Mockito.when(bookService.writeOffBook(bookId)).thenReturn(bookDto);

        mvc.perform(MockMvcRequestBuilders
                .post(String.format(EndpointConstant.BOOKS_WRITE_OFF.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBookDtoJson));
    }

    @Test
    void BookController_writeOffBook_shouldReturnErrorDto() throws Exception {
        Mockito.when(bookService.writeOffBook(bookId)).thenThrow(new BusinessException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders
                .post(String.format(EndpointConstant.BOOKS_WRITE_OFF.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void BookController_findSortedAllBooksByAvailability() throws Exception {
        Mockito.when(bookService.findSortedAllBooksByAvailability()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_BY_AVAILABILITY.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findSortedAllBooksByEditionYear() throws Exception {
        Mockito.when(bookService.findSortedAllBooksByEditionYear()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_BY_EDITION_YEAR.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findSortedAllBooksByPrice() throws Exception {
        Mockito.when(bookService.findSortedAllBooksByPrice()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_BY_PRICE.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findSortedAllBooksByReplenishmentDate() throws Exception {
        Mockito.when(bookService.findSortedAllBooksByReplenishmentDate()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_BY_REPLENISHMENT.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findSortedAllBooksByTitle() throws Exception {
        Mockito.when(bookService.findSortedAllBooksByTitle()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_BY_TITLE.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findSoldBooksBetweenDates() throws Exception {
        Mockito.when(bookService.findSoldBooksBetweenDates(Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(
                String.format(EndpointConstant.BOOKS_SOLD_BETWEEN_DATES.getEndpoint(), startDate, endDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findUnsoldBooksBetweenDates() throws Exception {
        Mockito.when(bookService.findUnsoldBooksBetweenDates(Mockito.any(Date.class), Mockito.any(Date.class)))
                .thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(
                String.format(EndpointConstant.BOOKS_UNSOLD_BETWEEN_DATES.getEndpoint(), startDate, endDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_findStaleBooks() throws Exception {
        Mockito.when(bookService.findStaleBooks()).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.get(EndpointConstant.BOOKS_STALE.getEndpoint()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_showBookDescription_shouldReturnBookDescriptionDto() throws Exception {
        Mockito.when(bookService.showBookDescription(Mockito.anyLong())).thenReturn(bookDescriptionDto);

        String expectedBookDescriptionDtoJson = "{\"title\":\"FirstTestBook\"," +
                "\"editionYear\":1000,\"replenishmentDate\":null}";
        mvc.perform(MockMvcRequestBuilders.get(String.format(EndpointConstant.BOOKS_DESCRIPTION.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBookDescriptionDtoJson));
    }

    @Test
    void BookController_showBookDescription_shouldReturnErrorDto() throws Exception {
        Mockito.when(bookService.showBookDescription(Mockito.anyLong())).thenThrow(new BusinessException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders.get(String.format(EndpointConstant.BOOKS_DESCRIPTION.getEndpoint(), bookId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void BookController_importBooks_shouldReturnImportedBooksDto() throws Exception {
        Mockito.when(bookService.importBooks(Mockito.anyString())).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.BOOKS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_importBooks_shouldReturnErrorDto() throws Exception {
        Mockito.when(bookService.importBooks(Mockito.anyString())).thenThrow(new BusinessException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.BOOKS_IMPORT.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), importFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }

    @Test
    void BookController_exportAllBooks() throws Exception {
        Mockito.when(bookService.exportAllBooks(Mockito.anyString())).thenReturn(booksDto);

        mvc.perform(MockMvcRequestBuilders.post(EndpointConstant.BOOKS_EXPORT_ALL.getEndpoint())
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBooksDtoJson));
    }

    @Test
    void BookController_exportBook_shouldReturnExportedBookDto() throws Exception {
        Mockito.when(bookService.exportBook(Mockito.anyLong(), Mockito.anyString())).thenReturn(bookDto);

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.BOOKS_EXPORT.getEndpoint(), bookId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedBookDtoJson));
    }

    @Test
    void BookController_exportBook_shouldReturnErrorDto() throws Exception {
        Mockito.when(bookService.exportBook(Mockito.anyLong(), Mockito.anyString()))
                .thenThrow(new BusinessException(exceptionMessage));

        mvc.perform(MockMvcRequestBuilders.post(String.format(EndpointConstant.BOOKS_EXPORT.getEndpoint(), bookId))
                .param(RequestParameterConstant.FILE_NAME.getParameterName(), exportFileName))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().json(expectedErrorDtoJson));
    }
}
