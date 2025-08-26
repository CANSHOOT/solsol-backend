package com.heyyoung.solsol.domain.settlement.dto;

import java.math.BigDecimal;

public record CouncilFeeView(
        Long feeId,
        Long councilId,
        String semester,
        BigDecimal feeAmount
) {}
