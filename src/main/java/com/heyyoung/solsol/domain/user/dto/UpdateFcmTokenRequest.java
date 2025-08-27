package com.heyyoung.solsol.domain.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * FCM 토큰 업데이트 요청 DTO
 */
@Getter
@NoArgsConstructor
public class UpdateFcmTokenRequest {
    private String fcmToken;
}