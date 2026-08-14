package com.library.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Library Management API")
                        .version("1.0")
                        .description("REST API for managing books, authors, categories, members and borrowing records.")
                        .contact(new Contact()
                                .name("Library Management Team"))
                        .license(new License()
                                .name("Internal / Educational Use")));
    }
}
