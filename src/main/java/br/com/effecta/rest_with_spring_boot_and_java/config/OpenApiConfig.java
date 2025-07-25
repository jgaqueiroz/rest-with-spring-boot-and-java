package br.com.effecta.rest_with_spring_boot_and_java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("REST API's RESTful with Java and Spring Boot")
                .version("v1")
                .description("REST API's RESTful with Java and Spring Boot")
                .termsOfService("https://effecta.com.br")
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://effecta.com.br")
                )
            );
    }
    
}
