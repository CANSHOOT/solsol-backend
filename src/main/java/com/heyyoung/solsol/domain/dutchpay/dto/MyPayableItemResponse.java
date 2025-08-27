package com.heyyoung.solsol.domain.dutchpay.dto;

import java.math.BigDecimal;

public record MyPayableItemResponse(
        Long groupId,
        String groupName,
        String organizerUserId,   // 👈 주최자 ID (내가 송금할 대상)
        String organizerName,     // 👈 주최자 이름
        BigDecimal settlementAmount,
        String status             // "진행중" / "완료" / "실패"
) {}
