package com.heyyoung.solsol.external.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 외부 API 설정 프로퍼티
 */
@Component
@ConfigurationProperties(prefix = "external-api.fin-open-api")
@Getter
@Setter
public class ExternalApiProperties {
    
    private String baseUrl;
    private String managerId;
    private String apiKey;
}