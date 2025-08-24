package com.heyyoung.solsol.domain.dutchpay.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 송금 처리 응답 DTO
 */
@Getter
@Builder
public class PaymentResponse {
    
    private String transactionId;
    private BigDecimal amount;
    private String status;
    private String message;

    /**
     * 송금 성공 응답 생성
     * @param transactionId 거래 ID
     * @param amount 송금 금액
     * @return PaymentResponse
     */
    public static PaymentResponse success(String transactionId, BigDecimal amount) {
        return PaymentResponse.builder()
                .transactionId(transactionId)
                .amount(amount)
                .status("SUCCESS")
                .message("송금이 완료되었습니다.")
                .build();
    }

    /**
     * 송금 실패 응답 생성
     * @param message 실패 메시지
     * @return PaymentResponse
     */
    public static PaymentResponse failure(String message) {
        return PaymentResponse.builder()
                .status("FAILURE")
                .message(message)
                .build();
    }
}