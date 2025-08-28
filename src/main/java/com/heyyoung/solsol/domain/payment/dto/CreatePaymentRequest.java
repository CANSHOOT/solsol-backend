package com.heyyoung.solsol.domain.payment.dto;

import java.math.BigDecimal;

public record CreatePaymentRequest(BigDecimal amount, long discountCouponId) {
}
