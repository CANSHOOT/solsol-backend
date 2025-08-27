package com.heyyoung.solsol.domain.dutchpay.dto;

import java.math.BigDecimal;

public record MyReceivableItemResponse(
        Long groupId,
        String groupName,
        String userId,        // 참가자 ID (돈을 보낼 사람)
        String userName,      // 참가자 이름
        BigDecimal settlementAmount,
        String status         // "진행중" / "완료" / "실패"
) {}
