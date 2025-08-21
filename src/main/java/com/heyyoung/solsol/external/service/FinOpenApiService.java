package com.heyyoung.solsol.external.service;

import com.heyyoung.solsol.external.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 금융망 외부 API 호출을 담당하는 서맄스
 * API 키 재발급 및 사용자 계정 생성 기능을 제공
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinOpenApiService {

    private final WebClient webClient;

    @Value("${external-api.fin-open-api.base-url}")
    private String baseUrl;

    /**
     * API 키 재발급 요청
     * @param userId 사용자 이메일 (managerId로 사용)
     * @return API 키 재발급 응답
     */
    public ApiKeyReissueResponse reissueApiKey(String userId) {
        ApiKeyReissueRequest request = ApiKeyReissueRequest.builder()
                .managerId(userId)
                .build();

        return webClient.post()
                .uri(baseUrl + "/edu/app/reIssuedApiKey")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ApiKeyReissueResponse.class)
                .doOnError(error -> log.error("API Key 재발급 실패: {}", error.getMessage()))
                .block();
    }

    /**
     * 외부 API를 통한 사용자 계정 생성 또는 조회
     * 계정이 이미 존재하면 search API로 정보 조회
     * @param apiKey 재발급받은 API 키
     * @param email 사용자 이메일
     * @return 사용자 계정 정보 (userKey 포함)
     */
    public UserCreateResponse createUser(String apiKey, String email) {
        UserCreateRequest request = UserCreateRequest.builder()
                .apiKey(apiKey)
                .userId(email)
                .build();

        try {
            // 먼저 사용자 계정 생성 시도
            return webClient.post()
                    .uri(baseUrl + "/member/")
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(UserCreateResponse.class)
                    .block();
                    
        } catch (Exception e) {
            log.warn("사용자 계정 생성 실패, 기존 사용자 조회 시도: {}", e.getMessage());
            
            // E4002 오류인 경우 기존 사용자 정보 조회
            if (e.getMessage().contains("400") || e.getMessage().contains("E4002")) {
                return searchUser(apiKey, email);
            } else {
                throw e;
            }
        }
    }

    /**
     * 기존 사용자 정보 조회
     * @param apiKey API 키
     * @param email 사용자 이메일
     * @return 사용자 정보
     */
    public UserCreateResponse searchUser(String apiKey, String email) {
        UserCreateRequest request = UserCreateRequest.builder()
                .apiKey(apiKey)
                .userId(email)
                .build();

        return webClient.post()
                .uri(baseUrl + "/member/search")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserCreateResponse.class)
                .doOnError(error -> log.error("사용자 정보 조회 실패: {}", error.getMessage()))
                .block();
    }
}