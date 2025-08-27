package com.heyyoung.solsol.domain.payrequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PayRequestSummary {
    private Long payRequestId;
    private String requesterId;
    private Long groupId;
    private String memo;
    private BigDecimal totalAmount;
    private Instant createdAt;
}
