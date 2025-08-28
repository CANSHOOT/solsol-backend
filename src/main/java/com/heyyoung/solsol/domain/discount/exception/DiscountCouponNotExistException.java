package com.heyyoung.solsol.domain.discount.exception;

import com.heyyoung.solsol.common.exception.app.SolsolException;

public class DiscountCouponNotExistException extends SolsolException {
    public DiscountCouponNotExistException() {
        super(DiscountCouponErrorCode.DISCOUNT_COUPON_NOT_EXIST);
    }
}
