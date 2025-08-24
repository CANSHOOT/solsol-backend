package com.heyyoung.solsol.domain.dutchpay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 송금 처리 요청 DTO
 */
@Getter
@NoArgsConstructor
public class SendPaymentRequest {
    
    private String transactionSummary;
}