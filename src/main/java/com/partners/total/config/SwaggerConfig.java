package com.partners.total.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl("/");

        return new OpenAPI()
                .info(apiInfo())
                .servers(List.of(server));
    }

    private Info apiInfo() {
        return new Info()
                .title("Partners Test")
                .description("partner REST API ")
                .version("1.0.0");
    }
}
