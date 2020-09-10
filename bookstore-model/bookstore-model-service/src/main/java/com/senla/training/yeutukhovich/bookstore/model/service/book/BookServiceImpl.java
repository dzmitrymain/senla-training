package com.senla.training.yeutukhovich.bookstore.model.service.book;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Autowired;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.Singleton;
import com.senla.training.yeutukhovich.bookstore.dependencyinjection.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.exception.BusinessException;
import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.util.HibernateUtil;
import com.senla.training.yeutukhovich.bookstore.model.domain.Book;
import com.senla.training.yeutukhovich.bookstore.model.service.dto.BookDescription;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.MessageConstant;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;
import com.senla.training.yeutukhovich.bookstore.util.writer.FileDataWriter;
import org.hibernate.Session;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Singleton
public class BookServiceImpl implements BookService {

    @Autowired
    private BookDao bookDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private RequestDao requestDao;
    @Autowired
    private EntityCvsConverter entityCvsConverter;

    @ConfigProperty(propertyName = ApplicationConstant.CVS_DIRECTORY_PATH_PROPERTY_NAME)
    private String cvsDirectoryPath;
    @ConfigProperty
    private boolean requestAutoCloseEnabled;
    @ConfigProperty
    private byte staleMonthNumber;

    @Override
    public void replenishBook(Long id) {
        try {
            Session session = HibernateUtil.getCurrentSession();
            try {
                session.beginTransaction();
                Book book = bookDao.findById(id)
                        .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
                if (book.isAvailable()) {
                    throw new BusinessException(MessageConstant.BOOK_ALREADY_REPLENISHED.getMessage());
                }
                book.setAvailable(true);
                book.setReplenishmentDate(new Date());
                bookDao.update(book);
                if (requestAutoCloseEnabled) {
                    requestDao.closeRequestsByBookId(book.getId());
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
    }

    @Override
    public void writeOffBook(Long id) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            Book book = bookDao.findById(id)
                    .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
            if (!book.isAvailable()) {
                throw new BusinessException(MessageConstant.BOOK_ALREADY_WRITTEN_OFF.getMessage());
            }
            book.setAvailable(false);
            bookDao.update(book);
            session.getTransaction().commit();
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }


    @Override
    public List<Book> findSortedAllBooksByAvailability() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findSortedAllBooksByAvailability();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSortedAllBooksByEditionYear() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findSortedAllBooksByEditionYear();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSortedAllBooksByPrice() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            System.out.println();
            return bookDao.findSortedAllBooksByPrice();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSortedAllBooksByReplenishmentDate() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findSortedAllBooksByReplenishmentDate();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSortedAllBooksByTitle() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findSortedAllBooksByTitle();
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findSoldBooksBetweenDates(Date startDate, Date endDate) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findSoldBooksBetweenDates(startDate, endDate);
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findUnsoldBooksBetweenDates(Date startDate, Date endDate) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            return bookDao.findUnsoldBooksBetweenDates(startDate, endDate);
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public List<Book> findStaleBooks() {
        try (Session session = HibernateUtil.getCurrentSession()) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH, -staleMonthNumber);
            Date staleDate = new Date(calendar.getTimeInMillis());
            session.beginTransaction();
            return bookDao.findStaleBooksBetweenDates(staleDate, new Date());
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public BookDescription showBookDescription(Long id) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            Book book = bookDao.findById(id)
                    .orElseThrow(() -> new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage()));
            BookDescription bookDescription = new BookDescription();
            bookDescription.setTitle(book.getTitle());
            bookDescription.setEditionYear(book.getEditionYear());
            bookDescription.setReplenishmentDate(book.getReplenishmentDate());
            return bookDescription;
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public int exportAllBooks(String fileName) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
            List<String> bookStrings = entityCvsConverter.convertBooks(bookDao.findAll());
            return FileDataWriter.writeData(path, bookStrings);
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public void exportBook(Long bookId, String fileName) {
        try (Session session = HibernateUtil.getCurrentSession()) {
            session.beginTransaction();
            String path = cvsDirectoryPath
                    + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
            Optional<Book> bookOptional = bookDao.findById(bookId);
            if (bookOptional.isEmpty()) {
                throw new BusinessException(MessageConstant.BOOK_NOT_EXIST.getMessage());
            }
            Book book = bookOptional.get();
            List<String> bookStrings = entityCvsConverter.convertBooks(List.of(book));
            FileDataWriter.writeData(path, bookStrings);
        } catch (BusinessException e) {
            throw e;
        } catch (Throwable e) {
            throw new InternalException(e.getMessage());
        }
    }

    @Override
    public int importBooks(String fileName) {
        if (fileName == null) {
            return 0;
        }
        int importedBooksNumber = 0;
        List<String> dataStrings = readStringsFromFile(fileName);
        List<Book> importedBooks = entityCvsConverter.parseBooks(dataStrings);
        try {
            Session session = HibernateUtil.getCurrentSession();
            try {
                session.beginTransaction();
                List<Book> repoBooks = bookDao.findAll();
                session.clear();
                for (Book importedBook : importedBooks) {
                    if (repoBooks.contains(importedBook)) {
                        bookDao.update(importedBook);
                        if (importedBook.isAvailable()) {
                            requestDao.closeRequestsByBookId(importedBook.getId());
                        }
                    } else {
                        bookDao.add(importedBook);
                    }
                    importedBooksNumber++;
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
        return importedBooksNumber;
    }

    private List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE;
        return FileDataReader.readData(path);
    }
}
