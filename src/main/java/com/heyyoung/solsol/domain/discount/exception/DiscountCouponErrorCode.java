package com.heyyoung.solsol.domain.discount.exception;

import com.heyyoung.solsol.common.exception.app.ErrorCode;

public enum DiscountCouponErrorCode implements ErrorCode {
    DISCOUNT_COUPON_NOT_EXIST(404, "해당 쿠폰은 존재하지 않습니다.");

    private final int statusCode;
    private final String message;

    DiscountCouponErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public int getStatusCode() {
        return this.statusCode;
    }
}
