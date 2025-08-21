package com.heyyoung.solsol.common.exception;

import com.heyyoung.solsol.common.exception.api.ApiErrorCode;
import com.heyyoung.solsol.common.exception.api.ApiErrorResponse;
import com.heyyoung.solsol.common.exception.api.ApiException;
import com.heyyoung.solsol.common.exception.app.ErrorCode;
import com.heyyoung.solsol.common.exception.app.ErrorResponse;
import com.heyyoung.solsol.common.exception.app.SolsolException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(SolsolException.class)
    public ResponseEntity<ErrorResponse> handleSolsolException(SolsolException e) {
        ErrorCode error = e.getErrorCode();
        int code = error.getStatusCode();
        String message = error.getMessage();

        return ResponseEntity.status(code).body(new ErrorResponse(code, message));
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleSolsolException(ApiException e) {
        ApiErrorCode error = e.getErrorCode();
        String code = error.getCode();
        String message = error.getMessage();

        return ResponseEntity.internalServerError().body(new ApiErrorResponse(code, message));
    }
}
