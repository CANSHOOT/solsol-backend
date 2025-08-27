package com.heyyoung.solsol.domain.discount.dto;

import java.time.LocalDate;

public record GetDiscountCouponResponse(long discountCouponId, int amount, LocalDate createdDate, LocalDate endDate) {
}
