package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.service.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class RequestServiceImplTest {

    private static final Book book = Mockito.mock(Book.class);
    private static final Request request = Mockito.mock(Request.class);
    private static final Long bookId = 1L;
    private static final Long requestId = 1L;

    @Autowired
    private RequestService requestService;

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @BeforeAll
    static void setup() {
        Mockito.when(book.getId()).thenReturn(bookId);
        Mockito.when(request.getId()).thenReturn(requestId);
        Mockito.when(request.getBook()).thenReturn(book);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(bookDao);
        Mockito.reset(requestDao);
        Mockito.reset(entityCvsConverter);
    }

    @Test
    void RequestServiceImpl_createRequest_shouldUpdateIfBookExistAndNotAvailable() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(false);

        requestService.createRequest(bookId, "");

        Mockito.verify(requestDao, Mockito.times(1)).add(Mockito.any());
    }

    @Test
    void RequestServiceImpl_createRequest_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class,
                () -> requestService.createRequest(bookId, ""));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void RequestServiceImpl_createRequest_shouldThrowExceptionIfBookAvailable() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(true);

        Throwable exception = Assertions.assertThrows(BusinessException.class,
                () -> requestService.createRequest(bookId, ""));

        Assertions.assertEquals(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage(), exception.getMessage());
    }

    @Test
    void RequestServiceImpl_findSortedAllRequestsByBookTitle() {
        requestService.findSortedAllRequestsByBookTitle();

        Mockito.verify(requestDao, Mockito.times(1)).findSortedAllRequestsByBookTitle();
    }

    @Test
    void RequestServiceImpl_findSortedAllRequestsByIsActive() {
        requestService.findSortedAllRequestsByIsActive();

        Mockito.verify(requestDao, Mockito.times(1)).findSortedAllRequestsByIsActive();
    }

    @Test
    void RequestServiceImpl_findSortedAllRequestsByRequesterData() {
        requestService.findSortedAllRequestsByRequesterData();

        Mockito.verify(requestDao, Mockito.times(1)).findSortedAllRequestsByRequesterData();
    }

    @Test
    void RequestServiceImpl_exportAllRequests() {
        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Assertions.assertNotNull(requestService.exportAllRequests(""));
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertRequests(Mockito.anyList());
        Mockito.verify(requestDao, Mockito.times(1)).findAll();
    }

    @Test
    void RequestServiceImpl_exportRequest() {
        Mockito.when(requestDao.findById(requestId)).thenReturn(Optional.of(request));

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            requestService.exportRequest(requestId, "");
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertRequests(Mockito.anyList());
        Mockito.verify(requestDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void RequestServiceImpl_exportRequest_shouldThrowExceptionIfRequestNotExist() {
        Mockito.when(requestDao.findById(requestId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Throwable exception = Assertions.assertThrows(BusinessException.class,
                    () -> requestService.exportRequest(requestId, ""));

            Assertions.assertEquals(MessageConstant.REQUEST_NOT_EXIST.getMessage(), exception.getMessage());
        }
    }

    @Test
    void RequestServiceImpl_importRequests() {
        Mockito.when(entityCvsConverter.parseRequests(Mockito.anyList())).thenReturn(List.of(request));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            requestService.importRequests("");
        }
        Mockito.verify(requestDao, Mockito.atLeastOnce()).update(request);
    }

    @Test
    void RequestServiceImpl_importRequests_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(entityCvsConverter.parseRequests(Mockito.anyList())).thenReturn(List.of(request));
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            Assertions.assertThrows(BusinessException.class, () -> requestService.importRequests(""));
        }
    }
}
