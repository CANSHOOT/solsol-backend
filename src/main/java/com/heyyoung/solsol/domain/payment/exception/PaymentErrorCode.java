package com.heyyoung.solsol.domain.payment.exception;

import com.heyyoung.solsol.common.exception.app.ErrorCode;

public enum PaymentErrorCode implements ErrorCode {

    PAYMENT_NOT_EXIST(404, "결제할 정보가 없습니다");

    private final int statusCode;
    private final String message;

    PaymentErrorCode(int statusCode, String message) {
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
