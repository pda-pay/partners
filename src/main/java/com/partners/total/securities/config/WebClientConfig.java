package com.partners.total.securities.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${app.key}")
    private String appkey;

    @Value("${app.secret}")
    private String appsecret;

    private final WebClient.Builder webClientBuilder;

    public WebClientConfig(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Bean
    public WebClient webClient() {
        return webClientBuilder
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .baseUrl("https://openapi.koreainvestment.com:9443")
                .build();
    }

    public String getAppkey() {
        return appkey;
    }

    public String getAppsecret() {
        return appsecret;
    }
}
