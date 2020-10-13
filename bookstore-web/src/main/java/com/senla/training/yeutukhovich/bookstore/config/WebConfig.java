package com.senla.training.yeutukhovich.bookstore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

@Configuration
@EnableWebMvc
@ComponentScan("com.senla.training.yeutukhovich.bookstore.controller")
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private Formatter<Date> formatter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(formatter);
    }
}
