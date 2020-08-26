package com.senla.training.yeutukhovich.bookstore.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.dao.connector.DbConnector;
import com.senla.training.yeutukhovich.bookstore.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class RequestServiceImpl implements RequestService {

    @Autowired
    private DbConnector connector;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    private RequestServiceImpl() {

    }

    @Override
    public void createRequest(Long bookId, String requesterData) {
        Connection connection = connector.getConnection();
        Optional<Book> bookOptional = bookDao.findById(connection, bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        if (bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        requestDao.add(connection, new Request(book, requesterData));
    }

    @Override
    public List<Request> findSortedAllRequestsByBookTitle() {
        return findAllRequests().stream()
                .sorted(Comparator.nullsLast(
                        Comparator.comparing(o -> o.getBook().getTitle())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Request> findSortedAllRequestsByIsActive() {
        return findAllRequests().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o2.isActive().compareTo(o1.isActive())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Request> findSortedAllRequestsByRequesterData() {
        return findAllRequests().stream()
                .sorted(Comparator.nullsLast(
                        Comparator.comparing(Request::getRequesterData)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int exportAllRequests(String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> requestStrings = entityCvsConverter.convertRequests(requestDao.findAll(connection));
        return FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    public void exportRequest(Long requestId, String fileName) {
        Connection connection = connector.getConnection();
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Request> requestOptional = requestDao.findById(connection, requestId);
        if (requestOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.REQUEST_NOT_EXIST.getMessage());
        }
        List<String> requestStrings = entityCvsConverter.convertRequests(List.of(requestOptional.get()));
        FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    public int importRequests(String fileName) {
        Connection connection = connector.getConnection();
        if (fileName == null) {
            return 0;
        }
        int importedRequestsNumber = 0;
        List<String> requestsStrings = readStringsFromFile(fileName);
        List<Request> repoRequests = requestDao.findAll(connection);
        List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);
        try {
            connection.setAutoCommit(false);
            try {
                for (Request importedRequest : importedRequests) {
                    Optional<Book> dependentBookOptional = bookDao.findById(connection, importedRequest.getBook().getId());
                    if (dependentBookOptional.isEmpty()) {
                        //log(MessageConstant.BOOK_NOT_NULL.getMessage());
                        continue;
                    }
                    importedRequest.setBook(dependentBookOptional.get());
                    if (repoRequests.contains(importedRequest)) {
                        requestDao.update(connection, importedRequest);
                    } else {
                        requestDao.add(connection, importedRequest);
                    }
                    importedRequestsNumber++;
                }
                connection.commit();
                return importedRequestsNumber;
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new InternalException(e.getMessage());
        }
    }

    private List<Request> findAllRequests() {
        return requestDao.findAll(connector.getConnection());
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        return FileDataReader.readData(path);
    }
}
