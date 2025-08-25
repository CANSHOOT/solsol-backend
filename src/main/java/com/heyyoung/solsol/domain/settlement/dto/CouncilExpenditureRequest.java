package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CouncilExpenditureRequest(
        Long councilId,
        BigDecimal amount,
        String description,
        Instant spentAt
) {}
