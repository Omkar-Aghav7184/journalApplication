package net.engineeringdigest.journalApp.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI()
    {
        return new OpenAPI()
                .info(new Info()
                        .title("Journal Application")
                        .description("API documentation for Journal Application by Omkar Aghav")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Omkar Aghav")
                                .url("https://github.com/Omkar-Aghav7184")
                                .email("omkar.t.aghav@gmail.com")))
                .servers(Arrays.asList(
                        new Server().url("http://localhost:8081/journal").description("Local Dev Server"),
                        new Server().url("http://localhost:9092/journalProd").description("Production Server")))
                .addSecurityItem(new SecurityRequirement()
                        .addList("bearerAuth"))
                .components(new Components().addSecuritySchemes(
                        "bearerAuth", new SecurityScheme()
                         .type(SecurityScheme.Type.HTTP)
                         .scheme("bearer")
                         .bearerFormat("JWT")
                         .in(SecurityScheme.In.HEADER)
                         .name("Authorization")
                ));
    }
}
