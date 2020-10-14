package com.senla.training.yeutukhovich.bookstore.config;

import com.senla.training.yeutukhovich.bookstore.util.formatter.DateFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.format.Formatter;

import java.util.Date;

@Configuration
@ComponentScan("com.senla.training.yeutukhovich.bookstore")
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        ppc.setLocation(new ClassPathResource("application.properties"));
        return ppc;
    }

    @Bean
    public Formatter<Date> dateFormatter() {
        return new DateFormatter();
    }
}
