package com.heyyoung.solsol.external.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserSearchRequest {
    private String userId;
    private String apiKey;
}