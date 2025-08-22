package com.heyyoung.solsol.external.service.member;

import com.heyyoung.solsol.external.dto.member.*;
import com.heyyoung.solsol.external.service.FinOpenApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 사용자 계정 관련 외부 API 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberApiService {

    private final FinOpenApiClient apiClient;

    /**
     * 사용자 계정 생성
     */
    public UserCreateResponse createUser(String email) {
        UserCreateRequest request = UserCreateRequest.builder()
                .apiKey(apiClient.getApiKey())
                .userId(email)
                .build();

        return apiClient.post("/member/", request, UserCreateResponse.class);
    }

    /**
     * 사용자 계정 조회
     */
    public UserSearchResponse searchUser(String email) {
        UserSearchRequest request = UserSearchRequest.builder()
                .apiKey(apiClient.getApiKey())
                .userId(email)
                .build();

        return apiClient.post("/member/search", request, UserSearchResponse.class);
    }
}