package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    private String cvsDirectoryPath = ApplicationConstant.CVS_DIRECTORY_PATH;

    @Override
    @Transactional
    public void createRequest(Long bookId, String requesterData) {
        Book book = bookDao.findById(bookId)
                .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
        if (book.isAvailable()) {
            throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
        }
        requestDao.add(new Request(book, requesterData));
    }

    @Override
    public List<Request> findSortedAllRequestsByBookTitle() {
        return requestDao.findSortedAllRequestsByBookTitle();
    }

    @Override
    public List<Request> findSortedAllRequestsByIsActive() {
        return requestDao.findSortedAllRequestsByIsActive();
    }

    @Override
    public List<Request> findSortedAllRequestsByRequesterData() {
        return requestDao.findSortedAllRequestsByRequesterData();
    }

    @Override
    public int exportAllRequests(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        List<String> requestStrings = entityCvsConverter.convertRequests(requestDao.findAll());
        return FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    public void exportRequest(Long requestId, String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        Optional<Request> requestOptional = requestDao.findById(requestId);
        if (requestOptional.isEmpty()) {
            throw new BusinessException(MessageConstant.REQUEST_NOT_EXIST.getMessage());
        }
        List<String> requestStrings = entityCvsConverter.convertRequests(List.of(requestOptional.get()));
        FileDataWriter.writeData(path, requestStrings);
    }

    @Override
    @Transactional
    public int importRequests(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedRequestsNumber = 0;
        List<String> requestsStrings = readStringsFromFile(fileName);
        List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);
        for (Request importedRequest : importedRequests) {
            Book dependentBook = bookDao.findById(importedRequest.getBook().getId())
                    .orElseThrow(() -> new BusinessException("Book can't be null."));
            importedRequest.setBook(dependentBook);
            requestDao.update(importedRequest);
            importedRequestsNumber++;
        }
        return importedRequestsNumber;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
