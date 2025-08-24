package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record CouncilExpenditureRow(
        Long expenditureId,
        Long councilId,
        BigDecimal amount,
        String category,
        String description,
        Instant expenditureDate
) {}
