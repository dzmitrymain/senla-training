package com.senla.training.yeutukhovich.bookstore.model.service.config;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCvsConverter;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookService;
import com.senla.training.yeutukhovich.bookstore.model.service.book.BookServiceImpl;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderService;
import com.senla.training.yeutukhovich.bookstore.model.service.order.OrderServiceImpl;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestService;
import com.senla.training.yeutukhovich.bookstore.model.service.request.RequestServiceImpl;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
@ComponentScan("com.senla.training.yeutukhovich.bookstore.model.service")
public class TestConfig {

    @Bean
    public BookDao bookDao() {
        return Mockito.mock(BookDao.class);
    }

    @Bean
    public OrderDao orderDao() {
        return Mockito.mock(OrderDao.class);
    }

    @Bean
    public RequestDao requestDao() {
        return Mockito.mock(RequestDao.class);
    }

    @Bean
    public EntityCvsConverter entityCvsConverter() {
        return Mockito.mock(EntityCvsConverter.class);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("/testApplication.properties"));
        ppc.setIgnoreResourceNotFound(true);
        return ppc;
    }
}
