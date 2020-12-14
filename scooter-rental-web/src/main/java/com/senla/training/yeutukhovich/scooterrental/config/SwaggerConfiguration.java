package com.senla.training.yeutukhovich.scooterrental.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan(basePackages = {"org.springdoc"})
public class SwaggerConfiguration extends WebMvcConfigurationSupport {

    private Info apiInfo() {
        return new Info().title("Scooter Rental Rest API").version("1.0.0")
                .description("This is a RESTful web service, final project at Senla training courses");
    }

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(apiInfo())
                .components(new Components()
                        .addSecuritySchemes("bearer-key", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .in(SecurityScheme.In.HEADER)
                                .scheme("bearer")
                                .bearerFormat("jwt")
                                .name("JWT Authentication")
                        )
                )
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearer-key")
                );
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
