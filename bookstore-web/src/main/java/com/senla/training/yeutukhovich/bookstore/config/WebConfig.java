package com.senla.training.yeutukhovich.bookstore.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.senla.training.yeutukhovich.bookstore.controller")
public class WebConfig implements WebMvcConfigurer {
}
