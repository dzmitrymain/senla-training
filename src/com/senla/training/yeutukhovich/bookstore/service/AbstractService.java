package com.senla.training.yeutukhovich.bookstore.service;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.dao.connector.DbConnector;
import com.senla.training.yeutukhovich.bookstore.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.util.constant.ApplicationConstant;
import com.senla.training.yeutukhovich.bookstore.util.constant.PropertyKeyConstant;
import com.senla.training.yeutukhovich.bookstore.util.injector.Autowired;
import com.senla.training.yeutukhovich.bookstore.util.injector.config.ConfigProperty;
import com.senla.training.yeutukhovich.bookstore.util.reader.FileDataReader;

import java.util.List;

public abstract class AbstractService {

    @Autowired
    protected DbConnector connector;

    @Autowired
    protected BookDao bookDao;
    @Autowired
    protected OrderDao orderDao;
    @Autowired
    protected RequestDao requestDao;

    @Autowired
    protected EntityCvsConverter entityCvsConverter;
    @ConfigProperty(propertyName = PropertyKeyConstant.CVS_DIRECTORY_KEY)
    protected String cvsDirectoryPath;

    protected List<String> readStringsFromFile(String fileName) {
        String path = cvsDirectoryPath
                + fileName + ApplicationConstant.CVS_FORMAT_TYPE.getConstant();
        return FileDataReader.readData(path);
    }
}
