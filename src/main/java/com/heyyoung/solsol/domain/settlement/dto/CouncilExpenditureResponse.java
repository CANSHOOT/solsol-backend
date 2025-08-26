package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CouncilExpenditureResponse(
        Long expenditureId,
        Long councilId,
        BigDecimal amount,
        String description,
        Instant spentAt,
        Instant createdAt
) {}
