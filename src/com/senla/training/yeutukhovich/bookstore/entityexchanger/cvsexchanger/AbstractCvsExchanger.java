package com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsexchanger;

import com.senla.training.yeutukhovich.bookstore.domain.Book;
import com.senla.training.yeutukhovich.bookstore.domain.Order;
import com.senla.training.yeutukhovich.bookstore.domain.Request;
import com.senla.training.yeutukhovich.bookstore.entityexchanger.cvsconverter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.repository.BookRepository;
import com.senla.training.yeutukhovich.bookstore.repository.IRepository;
import com.senla.training.yeutukhovich.bookstore.repository.OrderRepository;
import com.senla.training.yeutukhovich.bookstore.repository.RequestRepository;

// лучше оставить только пути, а репозитории перенести в реализации
// и еще архитектурный момент: лучше всего будет такая последовательность вызовов:
// котроллер - сервис (тут вызов репозиториев если надо) - импортер/экспортер (не вызывает репозитории)
public abstract class AbstractCvsExchanger {

    protected static EntityCvsConverter entityCvsConverter = EntityCvsConverter.getInstance();

    // нельзя хранить репозитории в статических полях (как и все остальные наши синглтоны), потом придется
    // переделывать, когда будет DI (депенденси инжекшн)
    protected static IRepository<Book> bookRepository = BookRepository.getInstance();
    protected static IRepository<Order> orderRepository = OrderRepository.getInstance();
    protected static IRepository<Request> requestRepository = RequestRepository.getInstance();

    // обычно константы пишут заглавными через нижнее подчеркивание DIRECTORY_PATH
    private static final String directoryPath = "./resources/cvs/";
    private static final String fileFormat = ".cvs";

    protected String buildPath(String fileName) {
        return directoryPath + fileName + fileFormat;
    }
}
