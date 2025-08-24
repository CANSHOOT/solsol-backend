package com.heyyoung.solsol.domain.dutchpay.dto;

import com.heyyoung.solsol.domain.dutchpay.entity.JoinMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 더치페이 참여 요청 DTO
 */
@Getter
@NoArgsConstructor
public class JoinDutchPayRequest {
    
    private JoinMethod joinMethod;
}