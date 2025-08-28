package com.heyyoung.solsol.domain.payment.exception;

import com.heyyoung.solsol.common.exception.app.ErrorCode;
import com.heyyoung.solsol.common.exception.app.SolsolException;

public class PaymentNotExistException extends SolsolException {
    public PaymentNotExistException() {
        super(PaymentErrorCode.PAYMENT_NOT_EXIST);
    }
}
