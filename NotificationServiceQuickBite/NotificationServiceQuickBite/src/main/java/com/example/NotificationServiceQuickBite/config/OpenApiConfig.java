package com.example.NotificationServiceQuickBite.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {//prueba git 2

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new Info()
                                .title("User Service QuickBite")
                                .version("1.0")
                                .description("Microservicio de usuarios")
                );
    }
}