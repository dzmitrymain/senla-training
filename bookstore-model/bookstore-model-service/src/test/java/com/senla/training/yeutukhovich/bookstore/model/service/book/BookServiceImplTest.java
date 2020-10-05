package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dto.BookDescriptionDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.config.TestConfig;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class BookServiceImplTest {

    private static final Book book = Mockito.mock(Book.class);
    private static final Long bookId = 1L;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @Value("${BookServiceImpl.requestAutoCloseEnabled:true}")
    private boolean requestAutoCloseEnabled;

    @BeforeAll
    static void setup() {
        Mockito.when(book.getId()).thenReturn(bookId);
    }

    @BeforeEach
    void setUp() {
        Mockito.reset(bookDao);
        Mockito.reset(requestDao);
        Mockito.reset(entityCvsConverter);
    }

    @Test
    void BookServiceImpl_replenishBook_shouldUpdateNotAvailableBookAndCloseRequests() {
        Assumptions.assumeTrue(requestAutoCloseEnabled);

        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(false);

        bookService.replenishBook(bookId);

        Mockito.verify(bookDao, Mockito.times(1)).update(book);
        Mockito.verify(requestDao, Mockito.times(1)).closeRequestsByBookId(bookId);
    }

    @Test
    void BookServiceImpl_replenishBook_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> bookService.replenishBook(bookId));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void BookServiceImpl_replenishBook_shouldThrowExceptionIfBookAvailable() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(true);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> bookService.replenishBook(bookId));

        Assertions.assertEquals(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage(), exception.getMessage());
    }

    @Test
    void BookServiceImpl_writeOffBook_shouldUpdateAvailableBook() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(true);

        bookService.writeOffBook(bookId);

        Mockito.verify(bookDao, Mockito.times(1)).update(book);
    }

    @Test
    void BookServiceImpl_writeOffBook_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> bookService.writeOffBook(bookId));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void BookServiceImpl_writeOffBook_shouldThrowExceptionIfBookNotAvailable() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
        Mockito.when(book.isAvailable()).thenReturn(false);

        Throwable exception = Assertions.assertThrows(BusinessException.class, () -> bookService.writeOffBook(bookId));

        Assertions.assertEquals(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage(), exception.getMessage());
    }

    @Test
    void BookServiceImpl_findSortedAllBooksByAvailability() {
        bookService.findSortedAllBooksByAvailability();

        Mockito.verify(bookDao, Mockito.times(1)).findSortedAllBooksByAvailability();
    }

    @Test
    void BookServiceImpl_findSortedAllBooksByEditionYear() {
        bookService.findSortedAllBooksByEditionYear();

        Mockito.verify(bookDao, Mockito.times(1)).findSortedAllBooksByEditionYear();
    }

    @Test
    void BookServiceImpl_findSortedAllBooksByPrice() {
        bookService.findSortedAllBooksByPrice();

        Mockito.verify(bookDao, Mockito.times(1)).findSortedAllBooksByPrice();
    }

    @Test
    void BookServiceImpl_findSortedAllBooksByReplenishmentDate() {
        bookService.findSortedAllBooksByReplenishmentDate();

        Mockito.verify(bookDao, Mockito.times(1)).findSortedAllBooksByReplenishmentDate();
    }

    @Test
    void BookServiceImpl_findSortedAllBooksByTitle() {
        bookService.findSortedAllBooksByTitle();

        Mockito.verify(bookDao, Mockito.times(1)).findSortedAllBooksByTitle();
    }

    @Test
    void BookServiceImpl_findSoldBooksBetweenDates() {
        bookService.findSoldBooksBetweenDates(Mockito.mock(Date.class), Mockito.mock(Date.class));

        Mockito.verify(bookDao, Mockito.times(1))
                .findSoldBooksBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void BookServiceImpl_findUnsoldBooksBetweenDates() {
        bookService.findUnsoldBooksBetweenDates(Mockito.mock(Date.class), Mockito.mock(Date.class));

        Mockito.verify(bookDao, Mockito.times(1))
                .findUnsoldBooksBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void BookServiceImpl_findStaleBooks() {
        bookService.findStaleBooks();

        Mockito.verify(bookDao, Mockito.times(1))
                .findStaleBooksBetweenDates(Mockito.any(), Mockito.any());
    }

    @Test
    void BookServiceImpl_showBookDescription_shouldReturnNotNull() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        BookDescriptionDto bookDescriptionDto = bookService.showBookDescription(bookId);

        Assertions.assertNotNull(bookDescriptionDto);
    }

    @Test
    void BookServiceImpl_showBookDescription_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        Throwable exception = Assertions.assertThrows(BusinessException.class,
                () -> bookService.showBookDescription(bookId));

        Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
    }

    @Test
    void BookServiceImpl_exportAllBooks() {
        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Assertions.assertNotNull(bookService.exportAllBooks(""));
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertBooks(Mockito.anyList());
        Mockito.verify(bookDao, Mockito.times(1)).findAll();
    }

    @Test
    void BookServiceImpl_exportBook() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.of(book));

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            bookService.exportBook(bookId, "");
        }
        Mockito.verify(entityCvsConverter, Mockito.times(1)).convertBooks(Mockito.anyList());
        Mockito.verify(bookDao, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    void BookServiceImpl_exportBook_shouldThrowExceptionIfBookNotExist() {
        Mockito.when(bookDao.findById(bookId)).thenReturn(Optional.empty());

        try (MockedStatic<FileDataWriter> mockedFileDataWriter = Mockito.mockStatic(FileDataWriter.class)) {
            Throwable exception = Assertions.assertThrows(BusinessException.class,
                    () -> bookService.exportBook(bookId, ""));

            Assertions.assertEquals(MessageConstant.BOOK_NOT_EXIST.getMessage(), exception.getMessage());
        }
    }

    @Test
    void BookServiceImpl_importBooks() {
        Mockito.when(entityCvsConverter.parseBooks(Mockito.anyList())).thenReturn(List.of(book));
        Mockito.when(book.isAvailable()).thenReturn(true);

        try (MockedStatic<FileDataReader> mockedFileDataReader = Mockito.mockStatic(FileDataReader.class)) {
            bookService.importBooks("");
        }
        Mockito.verify(bookDao, Mockito.atLeastOnce()).update(book);
        Mockito.verify(requestDao, Mockito.atLeastOnce()).closeRequestsByBookId(bookId);
    }
}
