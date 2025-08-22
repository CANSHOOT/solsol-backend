package com.heyyoung.solsol.external.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 금융 OpenAPI 관련 설정
 */
@Configuration
@RequiredArgsConstructor
public class FinOpenApiConfig {

    private final ExternalApiProperties properties;

    @Bean
    public WebClient finOpenApiWebClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}