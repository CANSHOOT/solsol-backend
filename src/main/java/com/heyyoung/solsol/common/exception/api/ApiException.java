package com.heyyoung.solsol.common.exception.api;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final ApiErrorCode errorCode;

    public ApiException(ApiErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
