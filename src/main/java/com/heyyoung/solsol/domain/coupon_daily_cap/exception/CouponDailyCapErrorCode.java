package com.heyyoung.solsol.domain.coupon_daily_cap.exception;

import com.heyyoung.solsol.common.exception.app.ErrorCode;

public enum CouponDailyCapErrorCode implements ErrorCode {
    NOT_EXIST(404, "오늘은 이벤트가 없습니다.");

    private final int statusCode;
    private final String message;

    CouponDailyCapErrorCode(int statusCode, String message) {
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
