package com.heyyoung.solsol.external.service.member;

import com.heyyoung.solsol.external.dto.member.UserCreateRequest;
import com.heyyoung.solsol.external.dto.member.UserCreateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 금융망 외부 API 호출을 담당하는 서맄스
 * API 키 재발급 및 사용자 계정 생성 기능을 제공
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberApiService {

    private final WebClient webClient;

    @Value("${external-api.fin-open-api.base-url}")
    private String baseUrl;

    @Value("${external-api.fin-open-api.manager-id}")
    private String managerId;

    @Value("${external-api.fin-open-api.api-key}")
    private String apiKey;

    /**
     * 외부 API를 통한 사용자 계정 생성
     * 전역 설정된 API Key를 사용하여 사용자 계정 생성
     * @param email 사용자 이메일
     * @return 사용자 계정 생성 응답 (userKey 포함)
     */
    public UserCreateResponse createUser(String email) {
        UserCreateRequest request = UserCreateRequest.builder()
                .apiKey(apiKey)
                .userId(email)
                .build();

        return webClient.post()
                .uri(baseUrl + "/member/")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserCreateResponse.class)
                .doOnError(error -> log.error("사용자 계정 생성 실패: {}", error.getMessage()))
                .block();
    }
}