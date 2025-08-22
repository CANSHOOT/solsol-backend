package com.heyyoung.solsol.external.service.common;

import com.heyyoung.solsol.external.dto.common.*;
import com.heyyoung.solsol.external.service.FinOpenApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 공통 외부 API 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CommonApiService {

    private final FinOpenApiClient apiClient;

    /**
     * 은행코드 조회
     */
    public BankCodeResponse inquireBankCodes() {
        CommonHeaderRequest header = createCommonHeader("inquireBankCodes", null);
        
        BankCodeRequest request = BankCodeRequest.builder()
                .Header(header)
                .build();

        return apiClient.post("/edu/bank/inquireBankCodes", request, BankCodeResponse.class);
    }

    /**
     * 공통 헤더 생성 (userKey 없는 버전)
     */
    private CommonHeaderRequest createCommonHeader(String apiName, String userKey) {
        LocalDateTime now = LocalDateTime.now();
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