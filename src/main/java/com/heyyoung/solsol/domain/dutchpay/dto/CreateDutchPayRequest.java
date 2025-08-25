package com.heyyoung.solsol.domain.dutchpay.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * 더치페이 생성 요청 DTO
 */
@Getter
@NoArgsConstructor
public class CreateDutchPayRequest {
    
    private Long paymentId;
    private String groupName;
    private BigDecimal totalAmount;
    private Integer participantCount;
    private List<String> participantUserIds;
}