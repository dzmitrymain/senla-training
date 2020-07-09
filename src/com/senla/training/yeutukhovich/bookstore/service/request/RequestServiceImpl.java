package com.senla.training.yeutukhovich.bookstore.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.IBookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Singleton
public class RequestServiceImpl implements RequestService {

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private RequestServiceImpl() {

    }

    @Override
    public Long createRequest(Long bookId, String requesterData) {
        Book checkedBook = bookRepository.findById(bookId);
        if (checkedBook != null && !checkedBook.isAvailable()) {
            Request request = new Request(checkedBook, requesterData);
            requestRepository.add(request);
            return request.getId();
        }
        return null;
    }

    @Override
    public List<Request> findSortedAllRequestsByBookTitle() {
        return findAllRequests().stream()
                .sorted(Comparator.nullsLast(
                        (o1, o2) -> o1.getBook().getTitle().compareTo(o2.getBook().getTitle())))
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
                        (o1, o2) -> o1.getRequesterData().compareTo(o2.getRequesterData())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public int exportAllRequests(String fileName) {
        ConfigInjector.injectConfig(this);
        int exportedRequestsNumber = 0;
        if (fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            List<String> requestStrings = entityCvsConverter.convertRequests(requestRepository.findAll());
            exportedRequestsNumber = FileDataWriter.writeData(path, requestStrings);

        }
        return exportedRequestsNumber;
    }

    @Override
    public boolean exportRequest(Long requestId, String fileName) {
        ConfigInjector.injectConfig(this);
        if (requestId != null && fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            Request request = requestRepository.findById(requestId);
            if (request != null) {
                List<String> requestStrings = entityCvsConverter.convertRequests(List.of(request));
                return FileDataWriter.writeData(path, requestStrings) != 0;
            }
        }
        return false;
    }

    @Override
    public int importRequests(String fileName) {
        ConfigInjector.injectConfig(this);
        int importedRequestsNumber = 0;
        if (fileName != null) {
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
            List<String> requestsStrings = FileDataReader.readData(path);

            List<Request> repoRequests = requestRepository.findAll();
            List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);

            for (Request importedRequest : importedRequests) {
                Book dependentBook = bookRepository.findById(importedRequest.getBook().getId());
                if (dependentBook == null) {
                    System.err.println(MessageConstant.BOOK_NOT_NULL.getMessage());
                    continue;
                }
                importedRequest.setBook(dependentBook);
                if (repoRequests.contains(importedRequest)) {
                    requestRepository.update(importedRequest);
                } else {
                    requestRepository.add(importedRequest);
                }
                importedRequestsNumber++;
            }
        }
        return importedRequestsNumber;
    }

    private List<Request> findAllRequests() {
        return requestRepository.findAll();
    }
}
