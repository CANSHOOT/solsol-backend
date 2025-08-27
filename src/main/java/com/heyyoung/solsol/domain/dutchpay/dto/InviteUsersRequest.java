package com.heyyoung.solsol.domain.dutchpay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * 사용자 초대 요청 DTO
 */
@Getter
@NoArgsConstructor
public class InviteUsersRequest {
    private List<String> userIds;  // 초대할 사용자 ID 목록
}