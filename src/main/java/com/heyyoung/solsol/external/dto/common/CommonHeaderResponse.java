package com.heyyoung.solsol.external.dto.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommonHeaderResponse {
    private String responseCode;
    private String responseMessage;
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
}