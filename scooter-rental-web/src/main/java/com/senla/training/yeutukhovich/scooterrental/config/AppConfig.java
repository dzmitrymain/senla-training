package com.senla.training.yeutukhovich.scooterrental.config;

import com.senla.training.yeutukhovich.scooterrental.util.constant.ApplicationConstant;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ComponentScan("com.senla.training.yeutukhovich.scooterrental")
public class AppConfig {

    @Bean
    public PropertySourcesPlaceholderConfigurer properties() {
        final PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
        ppc.setIgnoreResourceNotFound(true);
        ppc.setLocation(new ClassPathResource("application.properties"));
        return ppc;
    }


    @Bean
    public GeometryFactory geometryFactory() {
        return new GeometryFactory(new PrecisionModel(), ApplicationConstant.SRID_WGS84);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
