package com.heyyoung.solsol.domain.discount.dto;

import com.heyyoung.solsol.domain.discount.CouponType;

import java.time.LocalDate;

public record GetDiscountCouponResponse(long discountCouponId, int amount, LocalDate createdDate,
                                        LocalDate endDate, CouponType couponType) {
}
