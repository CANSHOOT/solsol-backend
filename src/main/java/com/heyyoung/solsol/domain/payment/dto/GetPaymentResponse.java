package com.heyyoung.solsol.domain.payment.dto;

import com.heyyoung.solsol.domain.payment.PaymentsEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;

public record GetPaymentResponse(long paymentId, int originalAmount, int discountAmount, int finalAmount,
                                 LocalDateTime date) {
    public static GetPaymentResponse from(PaymentsEntity payment) {
        return new GetPaymentResponse(payment.getPaymentId(),
                payment.getOriginalAmount().intValue(), payment.getDiscountAmount().intValue(),
                payment.getFinalAmount().intValue(), payment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
