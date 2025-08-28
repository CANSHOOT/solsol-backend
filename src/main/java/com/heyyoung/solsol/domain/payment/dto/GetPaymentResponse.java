package com.heyyoung.solsol.domain.payment.dto;

import com.heyyoung.solsol.domain.payment.PaymentsEntity;

import java.time.LocalDate;
import java.time.ZoneId;

public record GetPaymentResponse(long paymentId, int originalAmount, int discountAmount, int finalAmount,
                                 LocalDate date) {
    public static GetPaymentResponse from(PaymentsEntity payment) {
        return new GetPaymentResponse(payment.getPaymentId(),
                payment.getOriginalAmount().intValue(), payment.getDiscountAmount().intValue(),
                payment.getFinalAmount().intValue(), payment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDate());
    }
}
