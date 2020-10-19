package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCsvConverter;
import com.senla.training.yeutukhovich.bookstore.dto.RequestDto;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.mapper.RequestMapper;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.LoggerConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestServiceImpl implements RequestService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestServiceImpl.class);

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCsvConverter entityCsvConverter;
    @Autowired
    private RequestMapper requestMapper;

    private String csvDirectoryPath = ApplicationConstant.CSV_DIRECTORY_PATH;

    @Override
    @Transactional
    public RequestDto createRequest(Long bookId, String requesterData) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> {
                    LOGGER.warn(LoggerConstant.CREATE_REQUEST_FAIL.getMessage(), bookId,
                            MessageConstant.BOOK_NOT_EXIST.getMessage());
                    return new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage(), HttpStatus.NOT_FOUND);
                });
        if (book.getAvailable()) {
            LOGGER.warn(LoggerConstant.CREATE_REQUEST_FAIL.getMessage(), bookId,
                    MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage(), HttpStatus.FORBIDDEN);
        }
        Request request = new Request(book, requesterData);
        requestDao.add(request);
        LOGGER.info(LoggerConstant.CREATE_REQUEST_SUCCESS.getMessage(), bookId);
        return requestMapper.map(request);
    }

    @Override
    @Transactional
    public List<RequestDto> findSortedAllRequests(String sortParam) {
        List<Request> result;
        switch (sortParam) {
            case "TITLE":
                result = requestDao.findSortedAllRequestsByBookTitle();
                LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_BOOK_TITLE.getMessage());
                break;
            case "ACTIVE":
                result = requestDao.findSortedAllRequestsByIsActive();
                LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_IS_ACTIVE.getMessage());
                break;
            case "REQUESTER":
                result = requestDao.findSortedAllRequestsByRequesterData();
                LOGGER.info(LoggerConstant.FIND_ALL_REQUESTS_SORTED_BY_REQUESTER_DATA.getMessage());
                break;
            default:
                throw new BusinessException(String.format(MessageConstant.SORT_PARAM_NOT_SUPPORTED.getMessage(),
                        "TITLE, ACTIVE, REQUESTER"),
                        HttpStatus.BAD_REQUEST);
        }
        return result.stream()
                .map(requestMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RequestDto> exportAllRequests(String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        List<Request> requests = requestDao.findAll();
        List<String> requestStrings = entityCsvConverter.convertRequests(requests);
        FileDataWriter.writeData(path, requestStrings);
        LOGGER.info(LoggerConstant.EXPORT_ALL_REQUESTS.getMessage(), fileName);
        return requests.stream()
                .map(requestMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestDto exportRequest(Long requestId, String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        Request request = requestDao.findById(requestId).orElseThrow(() -> {
            LOGGER.warn(LoggerConstant.EXPORT_REQUEST_FAIL.getMessage(), requestId,
                    MessageConstant.REQUEST_NOT_EXIST.getMessage());
            return new BusinessException(MessageConstant.REQUEST_NOT_EXIST.getMessage(), HttpStatus.NOT_FOUND);
        });
        List<String> requestStrings = entityCsvConverter.convertRequests(List.of(request));
        FileDataWriter.writeData(path, requestStrings);
        LOGGER.info(LoggerConstant.EXPORT_REQUEST_SUCCESS.getMessage(), requestId, fileName);
        return requestMapper.map(request);
    }

    @Override
    @Transactional
    public List<RequestDto> importRequests(String fileName) {
        List<String> requestsStrings = readStringsFromFile(fileName);
        List<Request> importedRequests = entityCsvConverter.parseRequests(requestsStrings);
        for (Request importedRequest : importedRequests) {
            Book dependentBook = bookDao.findById(importedRequest.getBook().getId())
                    .orElseThrow(() -> {
                        LOGGER.warn(LoggerConstant.IMPORT_REQUESTS_FAIL.getMessage(),
                                MessageConstant.BOOK_CANT_NULL.getMessage());
                        return new BusinessException(MessageConstant.BOOK_CANT_NULL.getMessage(), HttpStatus.BAD_REQUEST);
                    });
            importedRequest.setBook(dependentBook);
            requestDao.update(importedRequest);
        }
        LOGGER.info(LoggerConstant.IMPORT_REQUESTS_SUCCESS.getMessage(), fileName);
        return importedRequests.stream()
                .map(requestMapper::map)
                .collect(Collectors.toList());
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = csvDirectoryPath
                + fileName + ApplicationConstant.CSV_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
