package com.heyyoung.solsol.external.exception;

import com.heyyoung.solsol.common.exception.api.ApiErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum FinOpenApiErrorCode implements ApiErrorCode {
    
    // Header 관련 에러
    H1000("H1000", "HEADER 정보가 유효하지 않습니다."),
    H1001("H1001", "API 이름이 유효하지 않습니다."),
    H1002("H1002", "전송일자 형식이 유효하지 않습니다."),
    H1003("H1003", "전송시각 형식이 유효하지않습니다."),
    H1004("H1004", "기관코드가 유효하지않습니다."),
    H1005("H1005", "핀테크 앱 일련번호가 유효하지 않습니다."),
    H1006("H1006", "API 서비스코드가 유효하지 않습니다."),
    H1007("H1007", "기관거래고유번호가유효하지않습니다."),
    H1008("H1008", "기관거래고유번호가중복된 값입니다."),
    H1009("H1009", "API_KEY가 유효하지 않습니다."),
    H1010("H1010", "USER_KEY가 유효하지않습니다."),
    
    // Member 관련 에러
    E4001("E4001", "빈 데이터이거나 형식에 맞지 않는 데이터입니다."),
    E4002("E4002", "이미 존재하는 ID입니다."),
    E4003("E4003", "존재하지 않는 ID입니다."),
    E4004("E4004", "존재하지 않는 API KEY입니다."),
    
    // 기본 에러
    UNKNOWN_ERROR("UNKNOWN", "알 수 없는 오류가 발생했습니다.");
    
    private final String code;
    private final String message;
    
    /**
     * 외부 API 응답 코드를 내부 에러 코드로 매핑
     */
    public static FinOpenApiErrorCode fromCode(String code) {
        return Arrays.stream(values())
                .filter(errorCode -> errorCode.getCode().equals(code))
                .findFirst()
                .orElse(UNKNOWN_ERROR);
    }
}