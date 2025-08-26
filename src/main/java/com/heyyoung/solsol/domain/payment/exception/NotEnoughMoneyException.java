package com.heyyoung.solsol.domain.payment.exception;

import com.heyyoung.solsol.common.exception.api.ApiException;
import com.heyyoung.solsol.external.exception.FinOpenApiErrorCode;

public class NotEnoughMoneyException extends ApiException {
    public NotEnoughMoneyException() {
        super(FinOpenApiErrorCode.NOT_ENOUGH_MONEY);
    }
}
