package com.heyyoung.solsol.domain.payrequest.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePayRequest {
    private String requesterId;         // A(돈 받을 사람, String)
    private Long groupId;               // 더치페이 그룹 기반이면 전달, 개인 간이면 null
    private String memo;                // 선택
    private List<Item> participants;    // B, C 각각의 금액

    @Getter
    @NoArgsConstructor
    public static class Item {
        private String userId;          // 참가자(송금자, String)
        private BigDecimal amount;      // 낼 금액
    }
}
