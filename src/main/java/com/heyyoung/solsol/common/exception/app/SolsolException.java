package com.heyyoung.solsol.common.exception.app;

import lombok.Getter;

@Getter
public class SolsolException extends RuntimeException {
    private final ErrorCode errorCode;

    public SolsolException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
