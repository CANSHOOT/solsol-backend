package com.heyyoung.solsol.domain.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String userId;
    private String name;
}