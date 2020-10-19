package com.senla.training.yeutukhovich.bookstore.model.service.config;

import com.senla.training.yeutukhovich.bookstore.converter.EntityCsvConverter;
import com.senla.training.yeutukhovich.bookstore.model.dao.book.BookDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.order.OrderDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.request.RequestDao;
import com.senla.training.yeutukhovich.bookstore.model.dao.user.UserDao;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public UserDao userDao() {
        return Mockito.mock(UserDao.class);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return Mockito.mock(PasswordEncoder.class);
    }

    @Bean
    public EntityCsvConverter entityCvsConverter() {
        return Mockito.mock(EntityCsvConverter.class);
    }

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setLocation(new ClassPathResource("/testApplication.properties"));
        ppc.setIgnoreResourceNotFound(true);
        return ppc;
    }
}
