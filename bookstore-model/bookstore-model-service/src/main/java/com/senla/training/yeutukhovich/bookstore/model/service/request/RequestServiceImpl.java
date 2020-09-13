package com.senla.training.yeutukhovich.bookstore.model.service.request;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.util.HibernateUtil;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.domain.Request;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    @Value("${csv.directoryPath:resources/cvs/}")
    private String cvsDirectoryPath;

    @Override
    public void createRequest(Long bookId, String requesterData) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            Book book = bookDao.findById(bookId)
                    .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
            if (book.isAvailable()) {
                throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
            }
            requestDao.add(new Request(book, requesterData));
            session.getTransaction().commit();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Request> findSortedAllRequestsByBookTitle() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return requestDao.findSortedAllRequestsByBookTitle();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Request> findSortedAllRequestsByIsActive() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return requestDao.findSortedAllRequestsByIsActive();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Request> findSortedAllRequestsByRequesterData() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return requestDao.findSortedAllRequestsByRequesterData();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public int exportAllRequests(String fileName) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
            List<String> requestStrings = entityCvsConverter.convertRequests(requestDao.findAll());
            return FileDataWriter.writeData(path, requestStrings);
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public void exportRequest(Long requestId, String fileName) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
            Optional<Request> requestOptional = requestDao.findById(requestId);
            if (requestOptional.isEmpty()) {
                throw new BusinessException(MessageConstant.REQUEST_NOT_EXIST.getMessage());
            }
            List<String> requestStrings = entityCvsConverter.convertRequests(List.of(requestOptional.get()));
            FileDataWriter.writeData(path, requestStrings);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public int importRequests(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedRequestsNumber = 0;
        List<String> requestsStrings = readStringsFromFile(fileName);
        List<Request> importedRequests = entityCvsConverter.parseRequests(requestsStrings);
        try {
            Session session = HibernateUtil.getCurrentSession();
            try {
                session.beginTransaction();
                List<Request> repoRequests = requestDao.findAll();
                session.clear();
                for (Request importedRequest : importedRequests) {
                    Book dependentBook = bookDao.findById(importedRequest.getBook().getId())
                            .orElseThrow(() -> new BusinessException("Book can't be null."));
                    importedRequest.setBook(dependentBook);
                    if (repoRequests.contains(importedRequest)) {
                        requestDao.update(importedRequest);
                    } else {
                        requestDao.add(importedRequest);
                    }
                    importedRequestsNumber++;
                }
                session.getTransaction().commit();
            } catch (BusinessException e) {
                throw e;
            } catch (Throwable e) {
                session.getTransaction().rollback();
                throw new InternalException(e.getMessage());
            } finally {
                session.close();
            }
        } catch (BusinessException | InternalException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
        return importedRequestsNumber;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
