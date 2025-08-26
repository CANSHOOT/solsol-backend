package com.heyyoung.solsol.domain.coupon_daily_cap.exception;

import com.heyyoung.solsol.common.exception.app.SolsolException;

public class CouponDailyCapIsNotExistException extends SolsolException {
    public CouponDailyCapIsNotExistException() {
        super(CouponDailyCapErrorCode.NOT_EXIST);
    }
}
