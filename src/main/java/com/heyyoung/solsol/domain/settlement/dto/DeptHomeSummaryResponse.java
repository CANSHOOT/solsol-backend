package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;

public record DeptHomeSummaryResponse(
        Header header,
        BalanceCard balanceCard,
        FeeBadge feeBadge // null 가능
) {
    public record Header(
            Long departmentId,
            Long councilId,
            String councilName,
            String presidentUserId,
            String presidentName
    ) {}
    public record BalanceCard(BigDecimal currentBalance, BigDecimal monthSpendTotal, YearMonth month) {}
    public record FeeBadge(String semester, boolean paid, BigDecimal feeAmount, Instant paidAt) {}
}
