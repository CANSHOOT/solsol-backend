package com.heyyoung.solsol.external.service.common;

import com.heyyoung.solsol.external.dto.common.CommonHeaderRequest;
import com.heyyoung.solsol.external.service.FinOpenApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * 금융 API 공통 헤더 생성 서비스
 */
@Component
@RequiredArgsConstructor
public class CommonHeaderService {

    private final FinOpenApiClient apiClient;

    /**
     * 공통 헤더 생성
     * @param apiName API 명칭
     * @param userKey 사용자 키 (null 가능)
     * @return 생성된 공통 헤더 객체
     */
    public CommonHeaderRequest createCommonHeader(String apiName, String userKey) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String transmissionDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String transmissionTime = now.format(DateTimeFormatter.ofPattern("HHmmss"));
        String transactionUniqueNo = generateTransactionUniqueNo();

        return CommonHeaderRequest.builder()
                .apiName(apiName)
                .transmissionDate(transmissionDate)
                .transmissionTime(transmissionTime)
                .institutionCode("00100")
                .fintechAppNo("001")
                .apiServiceCode(apiName)
                .institutionTransactionUniqueNo(transactionUniqueNo)
                .apiKey(apiClient.getApiKey())
                .userKey(userKey)
                .build();
    }

    /**
     * 기관거래고유번호 생성 (YYYYMMDD + HHMMSS + 일련번호 6자리)
     */
    private String generateTransactionUniqueNo() {
        LocalDateTime now = LocalDateTime.now();
        String dateTime = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String serialNumber = String.format("%06d", (int) (Math.random() * 1000000));
        return dateTime + serialNumber;
    }
}