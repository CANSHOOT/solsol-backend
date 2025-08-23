package com.heyyoung.solsol.external.service;

import com.heyyoung.solsol.common.exception.api.ApiException;
import com.heyyoung.solsol.external.config.ExternalApiProperties;
import com.heyyoung.solsol.external.dto.error.ExternalApiErrorResponse;
import com.heyyoung.solsol.external.exception.FinOpenApiErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 금융 OpenAPI 공통 HTTP 클라이언트
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FinOpenApiClient {

    @Qualifier("finOpenApiWebClient")
    private final WebClient webClient;
    private final ExternalApiProperties properties;
    
    @Value("${external-api.fin-open-api.base-url}")
    private String baseUrl;

    /**
     * POST 요청 처리
     * @param uri 요청할 API 경로
     * @param request 요청 본문 객체
     * @param responseType 응답 타입 클래스
     * @return API 응답 객체
     */
    public <T, R> R post(String uri, T request, Class<R> responseType) {
        String fullUrl = baseUrl + uri;
        
        return WebClient.create()
                .post()
                .uri(fullUrl)
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ExternalApiErrorResponse.class)
                            .doOnNext(errorBody -> log.error("API 에러 응답 - URI: {}, Code: {}, Message: {}", 
                                    uri, errorBody.getResponseCode(), errorBody.getResponseMessage()))
                            .map(errorBody -> new ApiException(
                                    FinOpenApiErrorCode.fromCode(errorBody.getResponseCode())
                            ));
                })
                .bodyToMono(responseType)
                .doOnError(error -> log.error("API 호출 실패 - URI: {}, Error: {}", uri, error.getMessage()))
                .block();
    }

    /**
     * GET 요청 처리
     * @param uri 요청할 API 경로
     * @param responseType 응답 타입 클래스
     * @return API 응답 객체
     */
    public <R> R get(String uri, Class<R> responseType) {
        String fullUrl = baseUrl + uri;
        
        return WebClient.create()
                .get()
                .uri(fullUrl)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response -> {
                    return response.bodyToMono(ExternalApiErrorResponse.class)
                            .doOnNext(errorBody -> log.error("API 에러 응답 - URI: {}, Code: {}, Message: {}", 
                                    uri, errorBody.getResponseCode(), errorBody.getResponseMessage()))
                            .map(errorBody -> new ApiException(
                                    FinOpenApiErrorCode.fromCode(errorBody.getResponseCode())
                            ));
                })
                .bodyToMono(responseType)
                .doOnError(error -> log.error("API 호출 실패 - URI: {}, Error: {}", uri, error.getMessage()))
                .block();
    }

    /**
     * 공통 API Key 반환
     */
    public String getApiKey() {
        return properties.getApiKey();
    }

    /**
     * 공통 Manager ID 반환
     */
    public String getManagerId() {
        return properties.getManagerId();
    }
}