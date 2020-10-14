package com.senla.training.yeutukhovich.bookstore.runner;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.senla.training.yeutukhovich.bookstore.ui")
public class ApplicationConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        ppc.setLocation(new ClassPathResource("application.properties"));
        return ppc;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
