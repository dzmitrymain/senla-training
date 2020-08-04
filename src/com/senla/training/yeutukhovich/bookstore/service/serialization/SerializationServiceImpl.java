package com.senla.training.yeutukhovich.bookstore.service.serialization;

import com.senla.training.yeutukhovich.bookstore.exception.InternalException;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.ApplicationState;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;

@Singleton
public class SerializationServiceImpl implements SerializationService {

    @ConfigProperty(propertyName = PropertyKeyConstant.SERIALIZATION_DATA_PATH_KEY)
    private String serializationDataPath;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;

    private SerializationServiceImpl() {

    }

    @Override
    public void serializeBookstore() {

        ApplicationState applicationState = new ApplicationState(bookRepository.findAll()
                , orderRepository.findAll(), requestRepository.findAll());
        bookstoreSerializer.serializeBookstore(applicationState, serializationDataPath);
    }

    @Override
    public void deserializeBookstore() {
        try {
            ApplicationState applicationState = bookstoreSerializer.deserializeBookstore(serializationDataPath);
            applicationState.getBooks().forEach(book -> bookRepository.add(book));
            applicationState.getOrders().forEach(order -> orderRepository.add(order));
            applicationState.getRequests().forEach(request -> requestRepository.add(request));
        } catch (InternalException e) {
            EntityInitializer.initBooks().forEach(book -> bookRepository.add(book));
            throw e;
        }
    }
}
