package com.heyyoung.solsol.external.dto.error;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExternalApiErrorResponse {
    private String responseCode;
    private String responseMessage;
}