package com.heyyoung.solsol.external.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonHeaderRequest {
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    private String apiKey;
    private String userKey;
}