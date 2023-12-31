package com.latmn.kangaroo.platform.news.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Spring Boot REST API Documentation",
                description = "Spring Boot REST API Documentation",
                version = "v1.0",
                contact = @Contact(
                        name = "Elijah",
                        email = "xiaoyi@mail.com",
                        url = "https://wchar.net"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://wchar.net"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description = "Spring Boot  Management Documentation",
                url = "https://wchar.net"
        )
)
@Configuration
public class SpringDocConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("news")
                .displayName("新闻中心")
                .pathsToMatch("**", "/**")
                .build();
    }
}
