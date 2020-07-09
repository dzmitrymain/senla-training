package com.senla.training.yeutukhovich.bookstore.service.serialization;

import com.senla.training.yeutukhovich.bookstore.repository.IBookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IOrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRequestRepository;
import com.senla.training.yeutukhovich.bookstore.serializer.ApplicationState;
import com.senla.training.yeutukhovich.bookstore.serializer.BookstoreSerializer;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.initializer.EntityInitializer;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.Singleton;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigInjector;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;

@Singleton
public class SerializationServiceImpl implements SerializationService {

    @ConfigProperty(propertyName = PropertyKeyConstant.SERIALIZATION_DATA_PATH_KEY)
    private String serializationDataPath;

    @Autowired
    private IBookRepository bookRepository;
    @Autowired
    private IOrderRepository orderRepository;
    @Autowired
    private IRequestRepository requestRepository;

    @Autowired
    private BookstoreSerializer bookstoreSerializer;

    @Override
    public void serializeBookstore() {
        ConfigInjector.injectConfig(this);
        ApplicationState applicationState = new ApplicationState(bookRepository.findAll()
                , orderRepository.findAll(), requestRepository.findAll());
        bookstoreSerializer.serializeBookstore(applicationState, serializationDataPath);

    }

    @Override
    public void deserializeBookstore() {
        ConfigInjector.injectConfig(this);
        ApplicationState applicationState = bookstoreSerializer.deserializeBookstore(serializationDataPath);
        if (applicationState == null) {
            EntityInitializer.initBooks().forEach(book -> bookRepository.add(book));
            return;
        }
        applicationState.getBooks().forEach(book -> bookRepository.add(book));
        applicationState.getOrders().forEach(order -> orderRepository.add(order));
        applicationState.getRequests().forEach(request -> requestRepository.add(request));
    }
}
