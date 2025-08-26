package com.heyyoung.solsol.domain.settlement.dto;

public record StudentCouncilView(
        Long councilId,
        Long departmentId,
        Long accountId,
        String presidentId,  // ← DB가 BIGINT면 Long으로 변경
        String councilName,
        Boolean isActive
) {}
