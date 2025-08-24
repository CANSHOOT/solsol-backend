package com.heyyoung.solsol.common.exception.app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 솔솔 애플리케이션 에러 코드
 */
@Getter
@RequiredArgsConstructor
public enum SolsolErrorCode implements ErrorCode {

    // 공통 에러
    INVALID_REQUEST(400, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(500, "내부 서버 오류가 발생했습니다."),

    // 사용자 관련 에러
    USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_EXISTS(409, "이미 존재하는 사용자입니다."),
    INVALID_USER_CREDENTIALS(401, "잘못된 사용자 인증 정보입니다."),

    // 더치페이 관련 에러
    DUTCH_PAY_NOT_FOUND(404, "더치페이를 찾을 수 없습니다."),
    ALREADY_PARTICIPATED(409, "이미 참여한 더치페이입니다."),
    PARTICIPANT_NOT_FOUND(404, "더치페이 참여자를 찾을 수 없습니다."),
    INVALID_DUTCH_PAY_STATUS(400, "유효하지 않은 더치페이 상태입니다."),
    ALREADY_PAID(409, "이미 결제를 완료했습니다."),

    // 계좌 관련 에러
    ACCOUNT_NOT_FOUND(404, "계좌를 찾을 수 없습니다."),
    INSUFFICIENT_BALANCE(400, "잔액이 부족합니다."),

    // 결제 관련 에러
    PAYMENT_FAILED(500, "결제 처리에 실패했습니다."),
    PAYMENT_NOT_FOUND(404, "결제 정보를 찾을 수 없습니다.");

    private final int statusCode;
    private final String message;

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }
}