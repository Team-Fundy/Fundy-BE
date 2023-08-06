package com.fundy.FundyBE.global.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(appInfo());
    }

    private Info appInfo() {
        return new Info()
                .title("Fundy API Specification")
                .description("Fundy API 명세서입니다")
                .contact(new Contact()
                        .name("김동원")
                        .email("dongwon000103@gmail.com")
                        .url("Fundy Homepage"))
                .version("v0.1");
    }
}
