package com.heyyoung.solsol.external.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserCreateRequest {
    private String apiKey;
    private String userId;
}