package com.senla.training.yeutukhovich.bookstore.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.service.AbstractService;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class RequestServiceImpl extends AbstractService implements RequestService {

    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    private String cvsDirectoryPath;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private RequestServiceImpl() {

    }

    @Override
    public void createRequest(Long bookId, String requesterData) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if (bookOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
        }
        Book book = bookOptional.get();
        if (bookOptional.get().isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        requestRepository.add(new Request(book, requesterData));
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
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> requestStrings = entityCvsConverter.convertRequests(requestRepository.findAll());
        return FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    public void exportRequest(Long requestId, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        Optional<Request> requestOptional = requestRepository.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.REQUEST_NOT_EXIST.getMessage());
        }
        List<String> requestStrings = entityCvsConverter.convertRequests(List.of(requestOptional.get()));
        FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    public int importRequests(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedRequestsNumber = 0;

        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        List<String> requestsStrings = FileDataReader.readData(path);

        List<Request> repoRequests = requestRepository.findAll();
        List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);

        for (Request importedRequest : importedRequests) {
            Optional<Book> dependentBookOptional = bookRepository.findById(importedRequest.getBook().getId());
            if (dependentBookOptional.isEmpty()) {
                //log
                //System.err.println(MessageConstant.BOOK_NOT_NULL.getMessage());
                continue;
            }
            importedRequest.setBook(dependentBookOptional.get());
            if (repoRequests.contains(importedRequest)) {
                requestRepository.update(importedRequest);
            } else {
                requestRepository.add(importedRequest);
            }
            importedRequestsNumber++;
        }
        return importedRequestsNumber;
    }

    private List<Request> findAllRequests() {
        return requestRepository.findAll();
    }
}
