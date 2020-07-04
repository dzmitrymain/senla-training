package com.senla.training.yeutukhovich.bookstore.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IBookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRequestRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PathConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RequestServiceImpl implements RequestService {

    private static RequestService instance;

    private IBookRepository bookRepository;
    private IRequestRepository requestRepository;

    private RequestServiceImpl() {
        this.bookRepository = BookRepository.getInstance();
        this.requestRepository = RequestRepository.getInstance();
    }

    public static RequestService getInstance() {
        if (instance == null) {
            instance = new RequestServiceImpl();
        }
        return instance;
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
        int exportedRequestsNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
            List<String> requestStrings = EntityCvsConverter.getInstance().convertRequests(requestRepository.findAll());
            exportedRequestsNumber = FileDataWriter.writeData(path, requestStrings);

        }
        return exportedRequestsNumber;
    }

    @Override
    public boolean exportRequest(Long requestId, String fileName) {
        if (requestId != null && fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
            Request request = requestRepository.findById(requestId);
            if (request != null) {
                List<String> requestStrings = EntityCvsConverter.getInstance().convertRequests(List.of(request));
                return FileDataWriter.writeData(path, requestStrings) != 0;
            }
        }
        return false;
    }

    @Override
    public int importRequests(String fileName) {
        int importedRequestsNumber = 0;
        if (fileName != null) {
            String path = PathConstant.DIRECTORY_PATH.getPathConstant()
                    + fileName + PathConstant.CVS_FORMAT_TYPE.getPathConstant();
            List<String> requestsStrings = FileDataReader.readData(path);

            List<Request> repoRequests = requestRepository.findAll();
            List<Request> importedRequests = EntityCvsConverter.getInstance().parseRequests(requestsStrings);

            for (Request importedRequest : importedRequests) {
                Book dependentBook = bookRepository.findById(importedRequest.getBook().getId());
                if (dependentBook == null) {
                    // начинаем чистить бэкенд от sout - скоро будет логирование
                    // если нужно показать пользователю что-то на экране - передай через контроллер
                    // если не надо - убирай, потом добавишь в логи
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

    public void serializeRequests() {
        List<Request> requests = requestRepository.findAll();
        BookstoreSerializer.getInstance().serializeBookstore(requests,
                PathConstant.SERIALIZED_REQUESTS_PATH.getPathConstant());
    }

    public void deserializeRequests() {
        List<Request> requests = BookstoreSerializer.getInstance()
                .deserializeBookstore(PathConstant.SERIALIZED_REQUESTS_PATH.getPathConstant());
        if (requests != null) {
            requests.forEach(request -> requestRepository.add(request));
        }
    }

    private List<Request> findAllRequests() {
        return requestRepository.findAll();
    }
}
